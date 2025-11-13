package medlink.reservation.controller;

import lombok.RequiredArgsConstructor;
import medlink.common.response.ApiResponse;
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
}
