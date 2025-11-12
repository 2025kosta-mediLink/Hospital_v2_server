package medlink.doctor.repository;

import medlink.doctor.entity.Doctor;
import medlink.doctor.entity.DoctorWeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorWeeklyScheduleRepository extends JpaRepository<DoctorWeeklySchedule, Long> {
    List<DoctorWeeklySchedule> findAllByDoctor(Doctor doctor);
}