package medlink.prescription.prescription.dto;

import java.time.LocalDateTime;

/**
 * 처방전 요약 정보 DTO (React 클라이언트 전송용)
 */
public record PrescriptionSummary(
        Long prescriptionId,
        String departmentName,
        String doctorName,
        String treatmentDate,
        String status,
        String pharmacyName,
        LocalDateTime completedAt,
        boolean canSelect,
        boolean completed
) {
}


