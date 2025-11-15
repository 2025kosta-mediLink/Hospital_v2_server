package medlink.reception.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReceptionIdResponse {
    private Long receptionId;
    public static ReceptionIdResponse of(Long id) {
        return ReceptionIdResponse.builder().receptionId(id).build();
    }
}
