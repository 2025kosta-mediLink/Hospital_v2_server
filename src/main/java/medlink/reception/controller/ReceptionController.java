//package medlink.reception.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import medlink.common.exception.ErrorStatus;
//import medlink.common.exception.GlobalException;
//import medlink.common.response.ApiResponse;
//import medlink.common.util.AuthSessionUtil;
//import medlink.reception.dto.request.ReceptionCancelRequest;
//import medlink.reception.dto.request.ReceptionCreateRequest;
//import medlink.reception.dto.response.ReceptionIdResponse;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.net.URI;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v2/receptions")
//public class ReceptionController {
//
//  private final ReceptionCommandService receptionCommandService;
//  private final ReceptionQueryService receptionQueryService;
//
//  /** 접수 생성 */
//  @PostMapping
//  public ResponseEntity<ApiResponse<ReceptionIdResponse>> createReception(
//      HttpServletRequest req,
//      @Valid @RequestBody ReceptionCreateRequest request
//  ) {
//    String uuid = requireUuid(req);
//    Long id = receptionCommandService.createReception(uuid, request);
//    return ResponseEntity
//        .created(URI.create("/api/v2/receptions/" + id))
//        .body(ApiResponse.onSuccess(new ReceptionIdResponse(id)));
//  }
//
//  /** 접수 상세 */
//  @GetMapping("/{id}")
//  public ApiResponse<ReceptionDetailResponse> getReceptionDetail(@PathVariable Long id) {
//    return ApiResponse.onSuccess(receptionQueryService.getReceptionDetail(id));
//  }
//
//  /** 접수 취소 */
//  @PostMapping("/{id}/cancel")
//  public ApiResponse<ReceptionIdResponse> cancelReception(
//      HttpServletRequest req,
//      @PathVariable Long id,
//      @Valid @RequestBody ReceptionCancelRequest request
//  ) {
//    String uuid = requireUuid(req);
//    receptionCommandService.cancelReception(id, uuid, request.reason());
//    return ApiResponse.onSuccess(new ReceptionIdResponse(id));
//  }
//
//  /** 내 접수 목록 (상태/기간 필터) */
//  @GetMapping
//  public ApiResponse<List<ReceptionListItemResponse>> getMyReceptions(
//      HttpServletRequest req,
//      @RequestParam(defaultValue = "ALL") String status,
//      @RequestParam(required = false) String month,      // YYYY-MM
//      @RequestParam(required = false) String from,       // YYYY-MM-DD
//      @RequestParam(required = false) String to          // YYYY-MM-DD
//  ) {
//    String uuid = requireUuid(req);
//    List<ReceptionListItemResponse> list =
//        receptionQueryService.getMyReceptions(uuid, status, month, from, to);
//    return ApiResponse.onSuccess(list);
//  }
//
//  // ==== helpers ====
//  private String requireUuid(HttpServletRequest req) {
//    String uuid = AuthSessionUtil.getUuidOrNull(req);
//    if (uuid == null) throw new GlobalException(ErrorStatus.UNAUTHORIZED);
//    return uuid;
//  }
//
//
//
//}
