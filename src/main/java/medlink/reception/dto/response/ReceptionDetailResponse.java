package medlink.reception.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReceptionDetailResponse {
  private Long receptionId;
  private String receptionNo;
  private String type;
  private String status;
  private boolean consentNotice;
  private LocalDateTime consentAt;
  private String noteToDoctor;

  private Long memberId;
  private String memberName;

  private Long doctorId;
  private String doctorName;
  private Long departmentId;
  private String departmentName;

  private List<String> symptomNames;

  private Integer queueNo;
  private String waitingStatus;
  private Integer estimatedWaitMinutes;
  private LocalDateTime estimatedCallTime;
  private LocalDateTime calledAt;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
