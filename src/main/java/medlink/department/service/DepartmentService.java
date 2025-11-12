package medlink.department.service;

import lombok.RequiredArgsConstructor;
import medlink.department.dto.response.DepartmentResponse;
import medlink.department.entity.Department;
import medlink.department.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<DepartmentResponse> getAllDepartment() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(DepartmentResponse::from)
                .collect(Collectors.toList());
    }
}