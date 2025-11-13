package medlink.doctor.entity;

import jakarta.persistence.*;
import lombok.*;
import medlink.common.base.BaseTimeEntity;
import medlink.doctor.enums.ExceptionDayType;

import java.time.LocalDate;

@Entity
@Table(name = "doctor_exception_day")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DoctorExceptionDay extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exceptionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private LocalDate exceptionDate;

    @Column(length = 20, nullable = false)
    private ExceptionDayType type;
}
