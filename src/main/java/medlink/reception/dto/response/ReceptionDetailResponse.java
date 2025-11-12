package medlink.reception.dto.response;

import lombok.*;
import medlink.reception.entity.Reception;
import medlink.waiting.entity.WaitingTicket;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public static ReceptionDetailResponse from(Reception r,
                                               List<String> symptomNames,
                                               Optional<WaitingTicket> ticketOpt) {
        return ReceptionDetailResponse.builder()
                .receptionId(r.getReceptionId())
                .receptionNo(r.getReceptionNo())
                .type(r.getType().name())
                .status(r.getStatus().name())
                .consentNotice(r.isConsentNotice())
                .consentAt(r.getConsentAt())
                .noteToDoctor(r.getNoteToDoctor())
                .memberId(r.getMember().getMemberId())
                .memberName(r.getMember().getName())
                .doctorId(r.getDoctor().getDoctorId())
                .doctorName(r.getDoctor().getName())
                .departmentId(r.getDoctor().getDepartment().getDepartmentId())
                .departmentName(r.getDoctor().getDepartment().getName())
                .symptomNames(symptomNames)
                .queueNo(ticketOpt.map(WaitingTicket::getQueueNo).orElse(null))
                .waitingStatus(ticketOpt.map(t -> t.getStatus().name()).orElse(null))
                .estimatedWaitMinutes(ticketOpt.map(WaitingTicket::getEstimatedWaitMinutes).orElse(null))
                .estimatedCallTime(ticketOpt.map(WaitingTicket::getEstimatedCallTime).orElse(null))
                .calledAt(ticketOpt.map(WaitingTicket::getCalledAt).orElse(null))
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}
