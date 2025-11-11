package medlink.doctor.dto.response;

import lombok.*;
import medlink.doctor.entity.DoctorNotice;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DoctorNoticeResponse {
  // 공지 나타내는 날짜 끝난 건 조회하지 말기
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