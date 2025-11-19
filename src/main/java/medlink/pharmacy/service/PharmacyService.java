package medlink.pharmacy.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import medlink.pharmacy.dto.response.PharmacyResponse;
import medlink.pharmacy.repository.PharmacyJdbcRepository;
import medlink.prescription.repository.PrescriptionJdbcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyService {

	private final PharmacyJdbcRepository pharmacyJdbcRepository;
	private final PrescriptionJdbcRepository prescriptionJdbcRepository;

	/**
	 * 위치 기반 약국 검색.
	 * hospital_v1처럼 프론트엔드에서 카카오 API를 직접 호출하므로 빈 리스트 반환.
	 */
	public List<PharmacyResponse> searchNearby(double latitude, double longitude, int radiusMeters) {
		return List.of();
	}

	/**
	 * 약국 상세 조회.
	 * 외부 API 연동 전까지는 empty 반환.
	 */
	public Optional<PharmacyResponse> getDetail(String pharmacyId) {
		return Optional.empty();
	}

	/**
	 * 처방전을 약국으로 전송.
	 * pharmacy_prescription 레코드를 생성하고 pharmacy_prescription_id를 반환한다.
	 * 처방전이 없는 경우 자동으로 처방전을 생성한다.
	 */
	@Transactional
	public String sendPrescription(String pharmacyId, String pharmacyName, List<Long> prescriptionIds) {
		if (prescriptionIds == null || prescriptionIds.isEmpty()) {
			log.error("prescriptionIds is null or empty");
			throw new IllegalArgumentException("prescriptionIds must not be empty");
		}
		
		// 첫 번째 ID를 사용 (prescriptionId 또는 receptionId)
		Long firstId = prescriptionIds.get(0);
		log.info("Sending prescription with firstId={}, pharmacyId={}, pharmacyName={}", firstId, pharmacyId, pharmacyName);
		
		if (firstId == null) {
			log.error("firstId is null");
			throw new IllegalArgumentException("ID cannot be null. prescriptionId or receptionId must be provided.");
		}
		
		// 먼저 prescriptionId로 존재 여부 확인
		Long actualPrescriptionId;
		if (prescriptionJdbcRepository.existsPrescription(firstId)) {
			// prescriptionId로 존재하면 그대로 사용
			log.info("Found existing prescription with prescriptionId={}", firstId);
			actualPrescriptionId = firstId;
		} else {
			// prescriptionId로 없으면 receptionId로 처방전 찾기 시도
			log.info("Prescription not found with prescriptionId={}, trying to find by receptionId", firstId);
			Long foundPrescriptionId = prescriptionJdbcRepository.findPrescriptionIdByReceptionId(firstId);
			
			if (foundPrescriptionId != null) {
				// receptionId로 처방전을 찾았으면 사용
				log.info("Found prescription with receptionId={}, prescriptionId={}", firstId, foundPrescriptionId);
				actualPrescriptionId = foundPrescriptionId;
			} else {
				// 없으면 receptionId로 간주하여 처방전 생성 시도
				log.info("Prescription not found with receptionId={}, creating new prescription", firstId);
				try {
					actualPrescriptionId = prescriptionJdbcRepository.createPrescriptionForReception(firstId);
					log.info("Created new prescription with receptionId={}, prescriptionId={}", firstId, actualPrescriptionId);
				} catch (RuntimeException e) {
					log.error("Failed to create prescription for receptionId={}: {}", firstId, e.getMessage(), e);
					throw new IllegalArgumentException(
						"처방전을 생성할 수 없습니다. 접수내역 ID(" + firstId + ")가 존재하지 않거나 상태가 '진료완료(DONE)'가 아닙니다.", 
						e
					);
				}
			}
		}
		
		// pharmacy_prescription 레코드 생성
		try {
			Long pharmacyPrescriptionId = pharmacyJdbcRepository.createPharmacyPrescription(
				pharmacyId,
				pharmacyName,
				actualPrescriptionId,
				"START"
			);
			log.info("Created pharmacy_prescription with id={} for prescriptionId={}", pharmacyPrescriptionId, actualPrescriptionId);
			
			// pharmacy_prescription_id를 문자열로 변환하여 반환
			return String.valueOf(pharmacyPrescriptionId);
		} catch (Exception e) {
			log.error("Failed to create pharmacy_prescription for prescriptionId={}: {}", actualPrescriptionId, e.getMessage(), e);
			throw new RuntimeException("약국 처방전 전달 실패: " + e.getMessage(), e);
		}
	}
}