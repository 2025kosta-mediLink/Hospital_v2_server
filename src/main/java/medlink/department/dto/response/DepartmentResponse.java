package medlink.department.dto.response;

import lombok.*;
import medlink.department.entity.Department;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DepartmentResponse {

    private long departmentId;
    private String name;

    public static DepartmentResponse from(Department department) {
        return DepartmentResponse.builder()
                .departmentId(department.getDepartmentId())
                .name(department.getName())
                .build();
    }
}
