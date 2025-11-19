package medlink.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import medlink.common.base.BaseTimeEntity;
import medlink.doctor.entity.Doctor;
import medlink.member.entity.Member;
import medlink.reservation.enums.ReservationStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 20, nullable = false, unique = true)
    private String reservationNo;

    @Column(nullable = false)
    private LocalDateTime appointmentAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    public static Reservation of(Doctor doctor, Member member,
                                 String reservationNo, LocalDateTime appointmentAt,
                                 ReservationStatus status) {
        return Reservation.builder()
                .doctor(doctor)
                .member(member)
                .reservationNo(reservationNo)
                .appointmentAt(appointmentAt)
                .status(status)
                .build();
    }

//    public static Reservation updateStatus(Reservation reservation, ReservationStatus status) {
//        return Reservation.builder()
//                .reservationId(reservation.reservationId)
//                .doctor(reservation.doctor)
//                .member(reservation.member)
//                .reservationNo(reservation.reservationNo)
//                .appointmentAt(reservation.appointmentAt)
//                .status(status)
//                .build();
//    }

  /**
   * 예약 상태 변경 (인스턴스 메서드로 변경)
   */
  public void updateStatus(ReservationStatus newStatus) {
    this.status = newStatus;
  }


}
