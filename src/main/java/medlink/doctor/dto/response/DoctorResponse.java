package medlink.doctor.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DoctorResponse {
    private long doctorId;
    private String name;
    private String profileImageUrl;
    private ScheduleDTO Schedule;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Builder
    public static class ScheduleDTO {
        private Boolean monAm;
        private Boolean monPm;
        private Boolean tueAm;
        private Boolean tuePm;
        private Boolean wedAm;
        private Boolean wedPm;
        private Boolean thuAm;
        private Boolean thuPm;
        private Boolean friAm;
        private Boolean friPm;
        private Boolean satAm;
        private Boolean satPm;
    }
}
