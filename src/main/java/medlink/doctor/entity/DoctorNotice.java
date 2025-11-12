package medlink.doctor.entity;

import jakarta.persistence.*;
import lombok.*;
import medlink.common.base.BaseTimeEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_notice")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DoctorNotice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(length = 600, nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(nullable = false)
    private LocalDateTime endsAt;

    private Integer priority;
}
