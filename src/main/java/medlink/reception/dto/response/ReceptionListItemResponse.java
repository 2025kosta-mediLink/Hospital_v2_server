package medlink.reception.dto.response;

import lombok.*;
import medlink.reception.entity.Reception;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReceptionListItemResponse {
  private Long receptionId;
  private String receptionNo;
  private String type;
  private String status;
  private Long doctorId;
  private String doctorName;
  private String departmentName;
  private LocalDateTime createdAt;

    public static ReceptionListItemResponse from(Reception r) {
        return ReceptionListItemResponse.builder()
                .receptionId(r.getReceptionId())
                .receptionNo(r.getReceptionNo())
                .type(r.getType().name())
                .status(r.getStatus().name())
                .doctorId(r.getDoctor().getDoctorId())
                .doctorName(r.getDoctor().getName())
                .departmentName(r.getDoctor().getDepartment().getName())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
