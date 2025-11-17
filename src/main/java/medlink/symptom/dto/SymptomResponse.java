package medlink.symptom.dto;

import lombok.*;
import medlink.symptom.entity.Symptom;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SymptomResponse {

  private Long symptomId;
  private String name;

  /** 값으로 직접 생성 */
  public static SymptomResponse of(Long symptomId, String name) {
    return SymptomResponse.builder()
        .symptomId(symptomId)
        .name(name)
        .build();
  }

  /** 엔티티 → DTO 변환 */
  public static SymptomResponse from(Symptom e) {
    return SymptomResponse.builder()
        .symptomId(e.getSymptomId())
        .name(e.getName())
        .build();
  }

  /** 엔티티 리스트 → DTO 리스트 변환(편의 메서드) */
  public static List<SymptomResponse> fromEntities(List<Symptom> entities) {
    return entities.stream().map(SymptomResponse::from).toList();
  }
}
