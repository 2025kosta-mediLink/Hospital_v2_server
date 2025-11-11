package medlink.prescription.pharmacy.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import medlink.prescription.pharmacy.dto.PharmacySummary;
import org.springframework.stereotype.Service;

/**
 * 약국 검색/상세/전송 비즈니스 로직.
 *
 * <p>현재는 외부 공공 API를 프런트에서 직접 호출하고 있기 때문에, 서버에서는
 * 별도의 데이터베이스 접근 없이 빈 결과를 내려주도록 처리한다.
 * API 연동이 필요해질 경우, 이 클래스에서 RestTemplate/WebClient 등을 이용해
 * 외부 API를 호출하도록 확장하면 된다.</p>
 */
@Service
public class PharmacyService {

    /**
     * 위치 기반 약국 검색.
     * 현재는 프런트에서 외부 API를 호출하므로 빈 리스트 반환.
     */
    public List<PharmacySummary> searchNearby(double latitude, double longitude, int radiusMeters) {
        return List.of();
    }

    /**
     * 약국 상세 조회.
     * 외부 API 연동 전까지는 empty 반환.
     */
    public Optional<PharmacySummary> getDetail(String pharmacyId) {
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

 