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

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private WaitingTicketStatus status; // WAITING, CALLED, IN_SERVICE, DONE, CANCELLED, SKIPPED

  private Integer estimatedWaitMinutes;

  private LocalDateTime estimatedCallTime;

  private LocalDateTime calledAt;

  // === 비즈니스 메서드 ===

  /**
   * 환자 호출
   */
  public void call() {
    this.status = WaitingTicketStatus.CALLED;
    this.calledAt = LocalDateTime.now();
  }

  /**
   * 상태 변경
   */
  public void updateStatus(WaitingTicketStatus newStatus) {
    this.status = newStatus;
  }

  /**
   * 예상 대기 시간 업데이트
   */
  public void updateEstimatedWaitTime(int waitingCount) {
    this.estimatedWaitMinutes = waitingCount * 10; // 1인당 10분 가정
    this.estimatedCallTime = LocalDateTime.now().plusMinutes(this.estimatedWaitMinutes);
  }


}
