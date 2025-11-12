package medlink.doctor.service;

import lombok.RequiredArgsConstructor;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.department.entity.Department;
import medlink.department.repository.DepartmentRepository;
import medlink.doctor.dto.response.DoctorNoticeResponse;
import medlink.doctor.dto.response.DoctorResponse;
import medlink.doctor.entity.Doctor;
import medlink.doctor.entity.DoctorNotice;
import medlink.doctor.entity.DoctorWeeklySchedule;
import medlink.doctor.repository.DoctorNoticeRepository;
import medlink.doctor.repository.DoctorRepository;
import medlink.doctor.repository.DoctorWeeklyScheduleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DepartmentRepository departmentRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorWeeklyScheduleRepository doctorWeeklyScheduleRepository;
    private final DoctorNoticeRepository doctorNoticeRepository;

    public List<DoctorResponse> getDoctorsByDepartmentId(Long departmentId) {
        Department department = departmentRepository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.DEPARTMENT_NOT_FOUND));

        List<Doctor> doctors = doctorRepository.findAllByDepartmentOrderByNameAsc(department);
        if (doctors.isEmpty()) {
            throw new GlobalException(ErrorStatus.DOCTOR_NOT_REGISTERED);
        }

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

    public List<DoctorNoticeResponse> getDoctorNotices(Long doctorId) {
        List<DoctorNotice> notices =
                doctorNoticeRepository.findActiveNoticesByDoctorId(doctorId, LocalDateTime.now());
        if (notices.isEmpty()) {
            throw new GlobalException(ErrorStatus.DOCTOR_NOTICE_NOT_REGISTERED);
        }

        return notices.stream()
                .map(DoctorNoticeResponse::from)
                .toList();
    }
}
