package medlink.waiting.dto.response;

import lombok.*;
import medlink.waiting.entity.WaitingTicket;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class WaitingListResponse {
  private Long ticketId;
  private Long receptionId;
  private Integer queueNo;
  private String status;
  private String patientName;
  private String patientPhone; // chartNo 대신 전화번호 사용
  private LocalDateTime createdAt;
  private LocalDateTime calledAt;
  private Integer estimatedWaitMinutes;

  public static WaitingListResponse from(WaitingTicket ticket) {
    return WaitingListResponse.builder()
        .ticketId(ticket.getTicketId())
        .receptionId(ticket.getReception().getReceptionId())
        .queueNo(ticket.getQueueNo())
        .status(ticket.getStatus().name())
        .patientName(ticket.getReception().getMember().getName())
        .patientPhone(ticket.getReception().getMember().getPhone())
        .createdAt(ticket.getCreatedAt())
        .calledAt(ticket.getCalledAt())
        .estimatedWaitMinutes(ticket.getEstimatedWaitMinutes())
        .build();
  }
}