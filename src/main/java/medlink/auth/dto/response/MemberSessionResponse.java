package medlink.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberSessionResponse {
    private String uuid;

    public static MemberSessionResponse of(String uuid) {
        return MemberSessionResponse.builder()
                .uuid(uuid)
                .build();
    }
}
