package medlink.doctor.controller;

import lombok.RequiredArgsConstructor;
import medlink.common.response.ApiResponse;
import medlink.doctor.dto.response.DoctorResponse;
import medlink.doctor.service.DoctorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    /**
     * 부서 ID로 의사 목록 조회
     */
    @GetMapping("/list/department/{departmentId}")
    public ApiResponse<List<DoctorResponse>> getDoctorsByDepartmentId(
            @PathVariable Long departmentId) {

        List<DoctorResponse> doctorResponses =
                doctorService.getDoctorsByDepartmentId(departmentId);
        return ApiResponse.onSuccess(doctorResponses);
    }
}
