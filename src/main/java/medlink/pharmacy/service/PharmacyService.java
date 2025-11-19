package medlink.pharmacy.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import medlink.pharmacy.dto.response.PharmacyResponse;
import medlink.pharmacy.repository.PharmacyJdbcRepository;
import medlink.prescription.repository.PrescriptionJdbcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
			throw new IllegalArgumentException("prescriptionIds must not be empty");
		}
		
		// 첫 번째 ID를 사용 (prescriptionId 또는 receptionId)
		Long firstId = prescriptionIds.get(0);
		
		if (firstId == null) {
			throw new IllegalArgumentException("ID cannot be null. prescriptionId or receptionId must be provided.");
		}
		
		// 먼저 prescriptionId로 존재 여부 확인
		Long actualPrescriptionId;
		if (prescriptionJdbcRepository.existsPrescription(firstId)) {
			// prescriptionId로 존재하면 그대로 사용
			actualPrescriptionId = firstId;
		} else {
			// 없으면 receptionId로 간주하여 처방전 생성
			actualPrescriptionId = prescriptionJdbcRepository.createPrescriptionForReception(firstId);
		}
		
		// pharmacy_prescription 레코드 생성
		Long pharmacyPrescriptionId = pharmacyJdbcRepository.createPharmacyPrescription(
			pharmacyId,
			pharmacyName,
			actualPrescriptionId,
			"START"
		);
		
		// pharmacy_prescription_id를 문자열로 변환하여 반환
		return String.valueOf(pharmacyPrescriptionId);
	}
}