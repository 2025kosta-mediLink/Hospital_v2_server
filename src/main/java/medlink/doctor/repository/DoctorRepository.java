package medlink.doctor.repository;

import medlink.department.entity.Department;
import medlink.doctor.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllByDepartmentOrderByNameAsc(Department department);
}
