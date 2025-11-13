package medlink.reservation.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.common.response.ApiResponse;
import medlink.common.util.AuthSessionUtil;
import medlink.reservation.dto.request.ReservationRequest;
import medlink.reservation.dto.response.ReservationResponse;
import medlink.reservation.dto.response.ReservationTimesResponse;
import medlink.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
            HttpServletRequest req
    ) {
        String uuid = AuthSessionUtil.getUuid(req);
        Long reservationId = reservationService.createReservation(request, uuid);
        return ApiResponse.onSuccess(reservationId);
    }

    /**
     * 예약 단건 조회
     */
    @GetMapping("/{reservationId}")
    public ApiResponse<ReservationResponse> getReservation(
            @PathVariable Long reservationId,
            HttpServletRequest req
    ) {
        String uuid = AuthSessionUtil.getUuid(req);
        ReservationResponse response = reservationService.getReservation(reservationId, uuid);
        return ApiResponse.onSuccess(response);
    }


}
