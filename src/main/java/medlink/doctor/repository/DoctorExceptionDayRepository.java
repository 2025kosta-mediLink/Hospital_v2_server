package medlink.doctor.repository;

import medlink.doctor.entity.Doctor;
import medlink.doctor.entity.DoctorExceptionDay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface DoctorExceptionDayRepository extends JpaRepository<DoctorExceptionDay, Long> {
    boolean existsByDoctorAndExceptionDate(Doctor doctor, LocalDate date);
}
