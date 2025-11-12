package medlink.reservation.dto.response;

import lombok.*;
import medlink.reservation.entity.Reservation;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReservationListResponse {
    private long reservationId;
    private String reservationNo;
    private String departmentName;
    private String doctorName;
    private LocalDateTime appointmentAt;
    private String status;

    public static ReservationListResponse from(Reservation reservation) {
        return ReservationListResponse.builder()
                .reservationId(reservation.getReservationId())
                .reservationNo(reservation.getReservationNo())
                .departmentName(reservation.getDoctor().getDepartment().getName())
                .doctorName(reservation.getDoctor().getName())
                .appointmentAt(reservation.getAppointmentAt())
                .status(reservation.getStatus().name())  // ReservationStatus Enum 값을 문자열로 변환
                .build();
    }
}
