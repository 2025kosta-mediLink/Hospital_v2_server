package medlink.doctor.controller;

import lombok.RequiredArgsConstructor;
import medlink.common.response.ApiResponse;
import medlink.doctor.dto.response.DoctorNoticeResponse;
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
     * 부서별 의사 목록 조회
     */
    @GetMapping("/list/department/{departmentId}")
    public ApiResponse<List<DoctorResponse>> getDoctorsByDepartmentId(
            @PathVariable Long departmentId) {

        List<DoctorResponse> doctorResponses =
                doctorService.getDoctorsByDepartmentId(departmentId);
        return ApiResponse.onSuccess(doctorResponses);
    }

    /**
     * 의사별 공지사항 목록 조회
     */
    @GetMapping("/{doctorId}/notices")
    public ApiResponse<List<DoctorNoticeResponse>> getDoctorNotices(
            @PathVariable Long doctorId) {

        List<DoctorNoticeResponse> noticeResponses =
                doctorService.getDoctorNotices(doctorId);
        return ApiResponse.onSuccess(noticeResponses);
    }
}
