package medlink.prescription.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import medlink.common.util.AuthSessionUtil;
import medlink.member.repository.MemberRepository;
import medlink.member.entity.Member;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.prescription.dto.request.PrescriptionStatusUpdateRequest;
import medlink.prescription.dto.response.PrescriptionResponse;
import medlink.prescription.service.PrescriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 처방전 REST API 컨트롤러.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/prescriptions")
public class PrescriptionApiController {

    private final PrescriptionService prescriptionService;
    private final MemberRepository memberRepository;

    @GetMapping
    public ResponseEntity<List<PrescriptionResponse>> getPrescriptions(
            HttpServletRequest request
    ) {
        // 세션에서 로그인한 사용자의 uuid 가져오기
        String uuid = AuthSessionUtil.getUuid(request);
        
        // uuid로 Member 조회하여 memberId 가져오기
        Member member = memberRepository.findByUuid(uuid)
                .orElseThrow(() -> new GlobalException(ErrorStatus.MEMBER_NOT_FOUND));
        
        Long memberId = member.getMemberId();
        return ResponseEntity.ok(prescriptionService.getPrescriptions(memberId));
    }

    @PatchMapping("/{prescriptionId}/status")
    public ResponseEntity<Map<String, Boolean>> updateStatus(
            @PathVariable Long prescriptionId,
            @RequestBody PrescriptionStatusUpdateRequest request
    ) {
        prescriptionService.updateStatus(prescriptionId, request);
        return ResponseEntity.ok(Map.of("success", Boolean.TRUE));
    }
}


