package medlink.doctor.dto.response;

import lombok.*;
import medlink.doctor.entity.Doctor;
import medlink.doctor.entity.DoctorWeeklySchedule;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DoctorResponse {
    private long doctorId;
    private String name;
    private String profileImageUrl;
    private List<ScheduleDTO> schedules;

    public static DoctorResponse of(Doctor doctor, List<ScheduleDTO> schedules) {
        return DoctorResponse.builder()
                .doctorId(doctor.getDoctorId())
                .name(doctor.getName())
                .profileImageUrl(doctor.getProfileImageUrl())
                .schedules(schedules)
                .build();
    }

    // 내부 필드 DTO
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Builder
    public static class ScheduleDTO {
        private Integer dayOfWeek; // 요일 (1: 월, 2: 화, ..., 6: 토)
        private Boolean amFlag;   // 오전 진료 여부
        private Boolean pmFlag;   // 오후 진료 여부
    }

    public static ScheduleDTO from(DoctorWeeklySchedule schedule) {
        return ScheduleDTO.builder()
                .dayOfWeek(schedule.getDayOfWeek())
                .amFlag(schedule.isAmFlag())
                .pmFlag(schedule.isPmFlag())
                .build();
    }
}
