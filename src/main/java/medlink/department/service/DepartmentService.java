package medlink.department.service;

import lombok.RequiredArgsConstructor;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.department.dto.response.DepartmentResponse;
import medlink.department.entity.Department;
import medlink.department.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public List<DepartmentResponse> getAllDepartment() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(DepartmentResponse::from)
                .toList();
    }

    public Department getDepartmentById(Long departmentId) {
        return departmentRepository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new GlobalException(ErrorStatus.DEPARTMENT_NOT_FOUND));
    }
}