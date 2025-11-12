package medlink.doctor.service;

import lombok.RequiredArgsConstructor;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.department.entity.Department;
import medlink.department.repository.DepartmentRepository;
import medlink.doctor.dto.response.DoctorResponse;
import medlink.doctor.entity.Doctor;
import medlink.doctor.entity.DoctorWeeklySchedule;
import medlink.doctor.repository.DoctorRepository;
import medlink.doctor.repository.DoctorWeeklyScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorWeeklyScheduleRepository doctorWeeklyScheduleRepository;

    public List<DoctorResponse> getDoctorsByDepartmentId(Long departmentId) {
        Department department = departmentRepository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.DEPARTMENT_NOT_FOUND));

        List<Doctor> doctors = doctorRepository.findAllByDepartment(department);
        if (doctors.isEmpty()) {
            throw new GlobalException(ErrorStatus.DOCTOR_NOT_REGISTERED);
        }
        // 의사 이름순 정렬
        doctors.sort(Comparator.comparing(Doctor::getName));

        return doctors.stream()
                .map(doctor -> {
                    List<DoctorWeeklySchedule> schedules = doctorWeeklyScheduleRepository.findAllByDoctor(doctor);
                    List<DoctorResponse.ScheduleDTO> scheduleDTOs = schedules.stream()
                            .map(DoctorResponse::from)
                            .toList();

                    return DoctorResponse.of(doctor, scheduleDTOs);
                })
                .toList();
    }
}
