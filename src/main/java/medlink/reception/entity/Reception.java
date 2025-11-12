package medlink.reception.entity;

import jakarta.persistence.*;
import lombok.*;
import medlink.common.base.BaseTimeEntity;
import medlink.doctor.entity.Doctor;
import medlink.member.entity.Member;
import medlink.reservation.entity.Reservation;
import medlink.reception.enums  .ReceptionStatus;
import medlink.reception.enums.ReceptionType;

@Entity
@Table(name = "reception")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reception extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long receptionId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id")
  private Reservation reservation; // nullable

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "doctor_id", nullable = false)
  private Doctor doctor;

  @Column(length = 20, nullable = false)
  private String receptionNo;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReceptionType type; // RESERVATION, DIRECT

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReceptionStatus status; // WAITING, IN_SERVICE, DONE, CANCELLED

  @Column(nullable = false)
  private boolean consentNotice;

  @Column(nullable = false)
  private java.time.LocalDateTime consentAt;

  @Column(length = 500)
  private String noteToDoctor;

}
