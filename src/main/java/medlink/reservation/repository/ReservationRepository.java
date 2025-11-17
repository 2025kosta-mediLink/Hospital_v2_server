package medlink.reservation.repository;

import medlink.doctor.entity.Doctor;
import medlink.member.entity.Member;
import medlink.reservation.entity.Reservation;
import medlink.reservation.enums.ReservationStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // 의사, 날짜, 상태별 예약 목록 조회
    @Query("SELECT r FROM Reservation r WHERE r.doctor = :doctor " +
            "AND r.appointmentAt >= :startDateTime " +
            "AND r.appointmentAt < :endDateTime " +
            "AND r.status = :status " +
            "ORDER BY r.appointmentAt")
    List<Reservation> findAllByDoctorAndDateAndStatus(
            Doctor doctor,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            ReservationStatus status);

    // 회원별 예약 목록 조회 (날짜, 상태 필터링) + 정렬은 Sort로 받기
    @Query("SELECT r FROM Reservation r " +
            "WHERE r.member = :member " +
            "AND (:startAt IS NULL OR r.appointmentAt >= :startAt) " +
            "AND (:endAt IS NULL OR r.appointmentAt < :endAt) " +
            "AND (:status IS NULL OR r.status = :status)")
    List<Reservation> findAllByMemberAndFilters(
            Member member,
            LocalDateTime startAt,
            LocalDateTime endAt,
            ReservationStatus status,
            Sort sort
    );

    boolean existsByDoctorAndAppointmentAtAndStatus(
            Doctor doctor,
            LocalDateTime appointmentAt,
            ReservationStatus status
    );


}
