package medlink.waiting.dto.response;

import lombok.*;
import medlink.waiting.entity.WaitingTicket;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class WaitingTicketResponse {
  private Long ticketId;
  private Integer queueNo;
  private String status;
  private Integer estimatedWaitMinutes;
  private LocalDateTime estimatedCallTime;
  private LocalDateTime calledAt;
  private Integer waitingCount; // 앞에 대기 중인 인원

  public static WaitingTicketResponse from(WaitingTicket ticket) {
    return WaitingTicketResponse.builder()
        .ticketId(ticket.getTicketId())
        .queueNo(ticket.getQueueNo())
        .status(ticket.getStatus().name())
        .estimatedWaitMinutes(ticket.getEstimatedWaitMinutes())
        .estimatedCallTime(ticket.getEstimatedCallTime())
        .calledAt(ticket.getCalledAt())
        .build();
  }

  public static WaitingTicketResponse from(WaitingTicket ticket, int waitingCount) {
    return WaitingTicketResponse.builder()
        .ticketId(ticket.getTicketId())
        .queueNo(ticket.getQueueNo())
        .status(ticket.getStatus().name())
        .estimatedWaitMinutes(ticket.getEstimatedWaitMinutes())
        .estimatedCallTime(ticket.getEstimatedCallTime())
        .calledAt(ticket.getCalledAt())
        .waitingCount(waitingCount)
        .build();
  }
}