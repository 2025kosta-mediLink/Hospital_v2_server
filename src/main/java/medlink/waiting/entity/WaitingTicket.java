package medlink.waiting.entity;

import jakarta.persistence.*;
import lombok.*;
import medlink.common.base.BaseTimeEntity;
import medlink.reception.entity.Reception;
import medlink.waiting.enums.WaitingTicketStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "waiting_ticket")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WaitingTicket extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long ticketId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reception_id", nullable = false)
  private Reception reception;

  @Column(nullable = false)
  private Integer queueNo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WaitingTicketStatus status; // WAITING, CALLED, IN_SERVICE, DONE, CANCELLED, SKIPPED

  private Integer estimatedWaitMinutes;

  private LocalDateTime estimatedCallTime;

  private LocalDateTime calledAt;
}
