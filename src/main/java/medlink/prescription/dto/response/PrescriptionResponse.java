package medlink.prescription.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 처방전 요약 정보 DTO (React 클라이언트 전송용)
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class PrescriptionResponse {

    private Long prescriptionId;
    private Long receptionId;
    private String departmentName;
    private String doctorName;
    private String treatmentDate;
    private String status;
    private String pharmacyName;
    private LocalDateTime completedAt;
    private boolean canSelect;
    private boolean completed;

    public static PrescriptionResponse from(Row row) {
        return PrescriptionResponse.builder()
                .prescriptionId(row.prescriptionId())
                .receptionId(row.receptionId())
                .departmentName(row.departmentName())
                .doctorName(row.doctorName())
                .treatmentDate(row.treatmentDate())
                .status(row.status())
                .pharmacyName(row.pharmacyName())
                .completedAt(row.completedAt())
                .canSelect(row.canSelect())
                .completed(row.completed())
                .build();
    }

    public record Row(
            Long prescriptionId,
            Long receptionId,
            String departmentName,
            String doctorName,
            String treatmentDate,
            String status,
            String pharmacyName,
            LocalDateTime completedAt,
            boolean canSelect,
            boolean completed
    ) {}
}


