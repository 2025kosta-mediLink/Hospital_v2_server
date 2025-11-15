// src/main/java/medlink/reception/controller/ReceptionController.java
package medlink.reception.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.common.response.ApiResponse;
import medlink.common.util.AuthSessionUtil;
import medlink.reception.dto.request.ReceptionFromReservationCreateRequest;
import medlink.reception.service.ReceptionService;
import medlink.reception.dto.request.ReceptionCancelRequest;
import medlink.reception.dto.request.ReceptionCreateRequest;
import medlink.reception.dto.response.ReceptionDetailResponse;
import medlink.reception.dto.response.ReceptionIdResponse;
import medlink.reception.dto.response.ReceptionListItemResponse;
import medlink.reception.enums.ReceptionStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/receptions")
public class ReceptionController {

  private final ReceptionService receptionService;

  /**
   * 접수 생성
   */
  @PostMapping
  public ApiResponse<ReceptionIdResponse> createReception(
      HttpServletRequest httpReq,
      @Valid @RequestBody ReceptionCreateRequest request
  ) {
    String uuid = AuthSessionUtil.getUuid(httpReq);
    ReceptionIdResponse res = receptionService.createReception(uuid, request);
    return ApiResponse.onSuccess(res);
  }

  /**
   * 접수 상세
   */
  @GetMapping("/{id}")
  public ApiResponse<ReceptionDetailResponse> getReceptionDetail(@PathVariable Long id) {
    return ApiResponse.onSuccess(receptionService.getReceptionDetail(id));
  }

  /**
   * 접수 취소
   */
  @PostMapping("/{id}/cancel")
  public ApiResponse<ReceptionIdResponse> cancelReception(
      HttpServletRequest httpReq,
      @PathVariable Long id,
      @Valid @RequestBody ReceptionCancelRequest request
  ) {
    String uuid = AuthSessionUtil.getUuid(httpReq);
    receptionService.cancelReception(id, uuid, request.getReason());
    return ApiResponse.onSuccess(ReceptionIdResponse.of(id));
  }

  /**
   * 나의 접수 목록 조회 (상태/기간 필터)
   */
  @GetMapping("/list")
  public ApiResponse<List<ReceptionListItemResponse>> getMyReceptionList(
      HttpServletRequest httpReq,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Integer month,
      @RequestParam(required = false) ReceptionStatus status   // WAITING / IN_SERVICE / DONE / CANCELLED
  ) {
    String uuid = AuthSessionUtil.getUuid(httpReq);
    return ApiResponse.onSuccess(
        receptionService.getMyReceptionList(uuid, year, month, status)
    );
  }


  /**
   * 오늘 예약 건을 기반으로 접수 생성
   */
  @PostMapping("/from-reservation")
  public ApiResponse<ReceptionIdResponse> createReceptionFromReservation(
      @Valid @RequestBody ReceptionFromReservationCreateRequest request,
      HttpServletRequest httpReq
  ) {
    String uuid = AuthSessionUtil.getUuid(httpReq);

    Long receptionId = receptionService.createReceptionFromReservation(
        uuid,
        request
    );

    return ApiResponse.onSuccess(ReceptionIdResponse.of(receptionId));
  }


}
