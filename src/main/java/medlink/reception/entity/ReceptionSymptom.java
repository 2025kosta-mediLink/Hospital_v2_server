package medlink.reception.entity;

import jakarta.persistence.*;
import lombok.*;
import medlink.symptom.entity.Symptom;

import java.time.LocalDateTime;

@Entity
@Table(name = "reception_symptom")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReceptionSymptom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long receptionSymptomId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reception_id", nullable = false)
  private Reception reception;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "symptom_id", nullable = false)
  private Symptom symptom;

  @Column(nullable = false)
  private LocalDateTime createdAt; // 이 테이블은 DDL상 created_at만 존재
}
