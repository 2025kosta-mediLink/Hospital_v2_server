package medlink.reservation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.common.response.ApiResponse;
import medlink.common.util.AuthSessionUtil;
import medlink.reservation.dto.request.ReservationRequest;
import medlink.reservation.dto.response.ReservationListResponse;
import medlink.reservation.dto.response.ReservationResponse;
import medlink.reservation.dto.response.ReservationTimesResponse;
import medlink.reservation.dto.response.TodayReservationListResponse;
import medlink.reservation.enums.ReservationStatus;
import medlink.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 의사별 예약 가능 시간 목록 조회
     */
    @GetMapping("/doctor/{doctorId}/available-times")
    public ApiResponse<ReservationTimesResponse> getAvailableReservationTimes(
            @PathVariable Long doctorId,
            @RequestParam("date") LocalDate date) {
        ReservationTimesResponse availableTimes =
                reservationService.getAvailableReservationTimes(doctorId, date);
        return ApiResponse.onSuccess(availableTimes);
    }

    /**
     * 예약 생성
     */
    @PostMapping
    public ApiResponse<Long> createReservation(
            @Valid @RequestBody ReservationRequest request,
            HttpServletRequest httpReq
    ) {
        String uuid = AuthSessionUtil.getUuid(httpReq);
        Long reservationId = reservationService.createReservation(request, uuid);
        return ApiResponse.onSuccess(reservationId);
    }

    /**
     * 예약 단건 조회
     */
    @GetMapping("/{reservationId}")
    public ApiResponse<ReservationResponse> getReservation(
            @PathVariable Long reservationId,
            HttpServletRequest httpReq
    ) {
        String uuid = AuthSessionUtil.getUuid(httpReq);
        ReservationResponse response = reservationService.getReservation(reservationId, uuid);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 나의 예약 목록 조회
     */
    @GetMapping("/list")
    public ApiResponse<List<ReservationListResponse>> getReservationList(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) ReservationStatus status,
            HttpServletRequest httpReq
    ) {
        String uuid = AuthSessionUtil.getUuid(httpReq);
        List<ReservationListResponse> list =
                reservationService.getReservationList(uuid, year, month, status);
        return ApiResponse.onSuccess(list);
    }

    /**
     * 오늘의 예약 목록 조회
     */
    @GetMapping("/today")
    public ApiResponse<List<TodayReservationListResponse>> getTodayReservations(
            HttpServletRequest httpReq
    ) {
        String uuid = AuthSessionUtil.getUuid(httpReq);
        List<TodayReservationListResponse> list =
                reservationService.getTodayReservations(uuid);
        return ApiResponse.onSuccess(list);
    }

    /**
     * 예약 취소
     */
    @PostMapping("/{reservationId}/cancel")
    public ApiResponse<Void> cancelReservation(
            @PathVariable Long reservationId,
            HttpServletRequest httpReq
    ) {
        String uuid = AuthSessionUtil.getUuid(httpReq);
        reservationService.cancelReservation(reservationId, uuid);
        return ApiResponse.onSuccess(null);
    }
}
