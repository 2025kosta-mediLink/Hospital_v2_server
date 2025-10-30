package medlink.auth.dto.response;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberSessionResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String uuid;

    public static MemberSessionResponse of(String uuid) {
        return MemberSessionResponse.builder()
                .uuid(uuid)
                .build();
    }
}
