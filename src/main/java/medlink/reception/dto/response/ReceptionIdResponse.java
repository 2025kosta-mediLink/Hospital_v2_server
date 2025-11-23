package medlink.reception.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReceptionIdResponse {
    private Long receptionId;
    private Integer queueNo;  // 대기번호 추가

    public static ReceptionIdResponse of(Long id) {
        return ReceptionIdResponse.builder().receptionId(id).build();
    }

  //  대기번호 포함 생성 메서드 추가
  public static ReceptionIdResponse of(Long id, Integer queueNo) {
    return ReceptionIdResponse.builder()
        .receptionId(id)
        .queueNo(queueNo)
        .build();
  }
}
