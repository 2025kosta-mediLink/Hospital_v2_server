package medlink.prescription.entity;

import jakarta.persistence.*;
import lombok.*;
import medlink.common.base.BaseTimeEntity;
import medlink.doctor.entity.Doctor;
import medlink.reception.entity.Reception;

import java.time.LocalDateTime;

@Entity
@Table(name = "prescription")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Prescription extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long prescriptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reception_id", nullable = false)
    private Reception reception;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    @Column(length = 255)
    private String content;

    @Column(length = 100)
    private String pharmacyName;

    @Column
    private LocalDateTime completedDate;

    @Column(nullable = false)
    @Builder.Default
    private boolean completed = false;
}

