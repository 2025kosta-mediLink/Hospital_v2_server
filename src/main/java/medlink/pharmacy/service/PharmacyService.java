package medlink.pharmacy.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import medlink.pharmacy.dto.response.PharmacyResponse;
import org.springframework.stereotype.Service;

@Service
public class PharmacyService {

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
	 * 실제 연동이 없다면 UUID만 생성해서 반환한다.
	 */
	public String sendPrescription(String pharmacyId, List<Long> prescriptionIds) {
		return UUID.randomUUID().toString();
	}
}