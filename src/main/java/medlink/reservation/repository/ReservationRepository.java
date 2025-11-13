package medlink.reservation.repository;

import medlink.doctor.entity.Doctor;
import medlink.reservation.entity.Reservation;
import medlink.reservation.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
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

    boolean existsByDoctorAndAppointmentAtAndStatus(
            Doctor doctor,
            LocalDateTime appointmentAt,
            ReservationStatus status
    );


}
