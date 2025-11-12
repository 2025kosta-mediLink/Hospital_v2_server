package medlink.reception.dto.response;

import lombok.*;

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
}
