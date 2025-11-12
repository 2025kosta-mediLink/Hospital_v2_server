package medlink.doctor.entity;

import medlink.department.entity.Department;
import jakarta.persistence.*;
import lombok.*;
import medlink.common.base.BaseTimeEntity;

@Entity
@Table(name = "doctor")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Doctor extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id",nullable = false)
    private Department department;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 255)
    private String profileImageUrl;
}