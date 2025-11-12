package medlink.department.entity;

import jakarta.persistence.*;
import lombok.*;
import medlink.common.base.BaseTimeEntity;

@Entity
@Table(name = "department")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Department extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;

    @Column(length = 60, nullable = false)
    private String name;
}