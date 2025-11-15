// src/main/java/medlink/reception/controller/ReceptionController.java
package medlink.reception.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.common.response.ApiResponse;
import medlink.common.util.AuthSessionUtil;
import medlink.reception.ReceptionService;
import medlink.reception.dto.request.ReceptionCancelRequest;
import medlink.reception.dto.request.ReceptionCreateRequest;
import medlink.reception.dto.response.ReceptionDetailResponse;
import medlink.reception.dto.response.ReceptionIdResponse;
import medlink.reception.dto.response.ReceptionListItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/receptions")
public class ReceptionController {

    private final ReceptionService receptionService;

    /** 접수 생성 */
    @PostMapping
    public ResponseEntity<ApiResponse<ReceptionIdResponse>> createReception(
            HttpServletRequest req,
            @Valid @RequestBody ReceptionCreateRequest request
    ) {
        String uuid = requireUuid(req);
        ReceptionIdResponse res = receptionService.createReception(uuid, request);
        return ResponseEntity
                .created(URI.create("/api/v2/receptions/" + res.getReceptionId()))
                .body(ApiResponse.onSuccess(res));
    }

    /** 접수 상세 */
    @GetMapping("/{id}")
    public ApiResponse<ReceptionDetailResponse> getReceptionDetail(@PathVariable Long id) {
        return ApiResponse.onSuccess(receptionService.getReceptionDetail(id));
    }

    /** 접수 취소 */
    @PostMapping("/{id}/cancel")
    public ApiResponse<ReceptionIdResponse> cancelReception(
            HttpServletRequest req,
            @PathVariable Long id,
            @Valid @RequestBody ReceptionCancelRequest request
    ) {
        String uuid = requireUuid(req);
        receptionService.cancelReception(id, uuid, request.getReason());
        return ApiResponse.onSuccess(ReceptionIdResponse.of(id));
    }

    /** 내 접수 목록 (상태/기간 필터) */
    @GetMapping
    public ApiResponse<List<ReceptionListItemResponse>> getMyReceptions(
            HttpServletRequest req,
            @RequestParam(defaultValue = "ALL") String status,
            @RequestParam(required = false) String month,      // YYYY-MM
            @RequestParam(required = false) String from,       // YYYY-MM-DD
            @RequestParam(required = false) String to          // YYYY-MM-DD
    ) {
        String uuid = requireUuid(req);
        return ApiResponse.onSuccess(
                receptionService.getMyReceptions(uuid, status, month, from, to)
        );
    }

    // ==== helpers ====
    private String requireUuid(HttpServletRequest req) {
        String uuid = AuthSessionUtil.getUuid(req);
        if (uuid == null) throw new GlobalException(ErrorStatus.UNAUTHORIZED);
        return uuid;
    }
}
