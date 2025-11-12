package medlink.department.controller;

import lombok.RequiredArgsConstructor;
import medlink.common.response.ApiResponse;
import medlink.department.dto.response.DepartmentResponse;
import medlink.department.service.DepartmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * 전체 부서 목록 조회
     */
    @GetMapping("/list")
    public ApiResponse<List<DepartmentResponse>> getAllDepartment() {
        List<DepartmentResponse> departmentList = departmentService.getAllDepartment();
        return ApiResponse.onSuccess(departmentList);
    }
}
