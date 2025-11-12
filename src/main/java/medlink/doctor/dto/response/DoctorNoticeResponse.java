package medlink.doctor.dto.response;

import lombok.*;
import medlink.doctor.entity.DoctorNotice;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DoctorNoticeResponse {
    private long noticeId;
    private String content;
    private int priority;

    public static DoctorNoticeResponse from(DoctorNotice notice) {
        return DoctorNoticeResponse.builder()
                .noticeId(notice.getNoticeId())
                .content(notice.getContent())
                .priority(notice.getPriority())
                .build();
    }
}