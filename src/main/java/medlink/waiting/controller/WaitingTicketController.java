package medlink.waiting.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.common.response.ApiResponse;
import medlink.common.util.AuthSessionUtil;
import medlink.waiting.dto.request.CallPatientRequest;
import medlink.waiting.dto.request.UpdateWaitingStatusRequest;
import medlink.waiting.dto.response.WaitingListResponse;
import medlink.waiting.dto.response.WaitingTicketResponse;
import medlink.waiting.service.WaitingTicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/waiting")
public class WaitingTicketController {

  private final WaitingTicketService waitingTicketService;

  /**
   * 의사별 오늘의 대기 목록 조회
   */
  @GetMapping("/doctor/{doctorId}/today")
  public ApiResponse<List<WaitingListResponse>> getTodayWaitingList(
      @PathVariable Long doctorId,
      HttpServletRequest httpReq
  ) {
    String uuid = AuthSessionUtil.getUuid(httpReq);
    List<WaitingListResponse> list = waitingTicketService.getTodayWaitingList(doctorId, uuid);
    return ApiResponse.onSuccess(list);
  }

  /**
   * 환자 호출
   */
  @PostMapping("/call")
  public ApiResponse<WaitingTicketResponse> callPatient(
      @Valid @RequestBody CallPatientRequest request,
      HttpServletRequest httpReq
  ) {
    String uuid = AuthSessionUtil.getUuid(httpReq);
    WaitingTicketResponse response = waitingTicketService.callPatient(request, uuid);
    return ApiResponse.onSuccess(response);
  }

  /**
   * 대기 상태 업데이트 (진료 시작, 진료 완료 등)
   */
  @PatchMapping("/{ticketId}/status")
  public ApiResponse<Void> updateWaitingStatus(
      @PathVariable Long ticketId,
      @Valid @RequestBody UpdateWaitingStatusRequest request,
      HttpServletRequest httpReq
  ) {
    String uuid = AuthSessionUtil.getUuid(httpReq);
    waitingTicketService.updateWaitingStatus(ticketId, request, uuid);
    return ApiResponse.onSuccess(null);
  }

  /**
   * 환자가 대기 현황 조회
   */
  @GetMapping("/reception/{receptionId}")
  public ApiResponse<WaitingTicketResponse> getWaitingStatus(
      @PathVariable Long receptionId,
      HttpServletRequest httpReq
  ) {
    String uuid = AuthSessionUtil.getUuid(httpReq);
    WaitingTicketResponse response = waitingTicketService.getWaitingStatus(receptionId, uuid);
    return ApiResponse.onSuccess(response);
  }

  /**
   * 다음 대기자 자동 호출 (대기 1번 환자)
   */
  @PostMapping("/doctor/{doctorId}/call-next")
  public ApiResponse<WaitingTicketResponse> callNextPatient(
      @PathVariable Long doctorId,
      HttpServletRequest httpReq
  ) {
    String uuid = AuthSessionUtil.getUuid(httpReq);
    WaitingTicketResponse response = waitingTicketService.callNextPatient(doctorId, uuid);
    return ApiResponse.onSuccess(response);
  }
}