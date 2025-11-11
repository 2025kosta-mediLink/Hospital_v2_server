package medlink.prescription.prescription.dto;

import java.time.LocalDateTime;

/**
 * 처방전 상태 업데이트 요청 DTO.
 */
public record PrescriptionStatusUpdateRequest(
        String status,
        String pharmacyName,
        LocalDateTime completedAt
) {
}


