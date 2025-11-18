package medlink.doctor.service;

import lombok.RequiredArgsConstructor;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.department.entity.Department;
import medlink.department.service.DepartmentService;
import medlink.doctor.dto.response.DoctorNoticeResponse;
import medlink.doctor.dto.response.DoctorResponse;
import medlink.doctor.entity.Doctor;
import medlink.doctor.entity.DoctorNotice;
import medlink.doctor.entity.DoctorWeeklySchedule;
import medlink.doctor.repository.DoctorExceptionDayRepository;
import medlink.doctor.repository.DoctorNoticeRepository;
import medlink.doctor.repository.DoctorRepository;
import medlink.doctor.repository.DoctorWeeklyScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DoctorService {

    private final DepartmentService departmentService;
    private final DoctorRepository doctorRepository;
    private final DoctorWeeklyScheduleRepository weeklyScheduleRepository;
    private final DoctorNoticeRepository noticeRepository;
    private final DoctorExceptionDayRepository exceptionDayRepository;

    public List<DoctorResponse> getDoctorsByDepartmentId(Long departmentId) {
        Department department = departmentService.getDepartmentById(departmentId);
        List<Doctor> doctors = doctorRepository.findAllByDepartmentOrderByNameAsc(department);
        if (doctors.isEmpty()) {
            throw new GlobalException(ErrorStatus.DOCTOR_NOT_REGISTERED);
        }

        return doctors.stream()
                .map(doctor -> {
                    List<DoctorWeeklySchedule> schedules = weeklyScheduleRepository.findAllByDoctor(doctor);
                    List<DoctorResponse.ScheduleDTO> scheduleDTOs = schedules.stream()
                            .map(DoctorResponse::from)
                            .toList();

                    return DoctorResponse.of(doctor, scheduleDTOs);
                })
                .toList();
    }

    public List<DoctorNoticeResponse> getDoctorNotices(Long doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        List<DoctorNotice> notices =
                noticeRepository.findActiveNoticesByDoctor(doctor, LocalDateTime.now());

        // 프론트 axios 에러로 인한 주석처리(해당 의사의 공지사항이 없어도 에러처리 하지 않음)
//        if (notices.isEmpty()) {
//            throw new GlobalException(ErrorStatus.DOCTOR_NOTICE_NOT_REGISTERED);
//        }

      // 공지사항이 없어도 빈 리스트 반환 (정상 응답)
        return notices.stream()
                .map(DoctorNoticeResponse::from)
                .toList();
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.DOCTOR_NOT_FOUND));
    }

    public DoctorWeeklySchedule getDoctorWeeklyScheduleByDayOfWeek(Doctor doctor, int dayOfWeek) {
        return weeklyScheduleRepository.findByDoctorAndDayOfWeek(doctor, dayOfWeek)
                .orElseThrow(() -> new GlobalException(ErrorStatus.DOCTOR_WEEKLY_SCHEDULE_NOT_FOUND));
    }

    public void validateNotDoctorExceptionDay(
            Doctor doctor, LocalDate date) {
        if (exceptionDayRepository.existsByDoctorAndExceptionDate(doctor, date)) {
            throw new GlobalException(ErrorStatus.DOCTOR_ON_EXCEPTION_DAY);
        }
    }
}
