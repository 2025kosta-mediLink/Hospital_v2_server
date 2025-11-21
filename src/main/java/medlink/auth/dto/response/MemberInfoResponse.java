package medlink.auth.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberInfoResponse {
    private String uuid;
    private String loginId;
    private String name;
    private String rrn;

    public static MemberInfoResponse of(String uuid, String loginId, String name, String rrn) {
        return MemberInfoResponse.builder()
                .uuid(uuid)
                .loginId(loginId)
                .name(name)
                .rrn(rrn)
                .build();
    }
}

