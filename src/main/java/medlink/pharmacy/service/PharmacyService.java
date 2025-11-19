package medlink.pharmacy.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import medlink.pharmacy.dto.response.PharmacyResponse;
import medlink.pharmacy.repository.PharmacyJdbcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PharmacyService {

	private final PharmacyJdbcRepository pharmacyJdbcRepository;

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
	 */
	@Transactional
	public String sendPrescription(String pharmacyId, String pharmacyName, List<Long> prescriptionIds) {
		if (prescriptionIds == null || prescriptionIds.isEmpty()) {
			throw new IllegalArgumentException("prescriptionIds must not be empty");
		}
		
		// 첫 번째 처방전 ID를 사용하여 pharmacy_prescription 레코드 생성
		Long firstPrescriptionId = prescriptionIds.get(0);
		Long pharmacyPrescriptionId = pharmacyJdbcRepository.createPharmacyPrescription(
			pharmacyId,
			pharmacyName,
			firstPrescriptionId,
			"START"
		);
		
		// pharmacy_prescription_id를 문자열로 변환하여 반환
		return String.valueOf(pharmacyPrescriptionId);
	}
}