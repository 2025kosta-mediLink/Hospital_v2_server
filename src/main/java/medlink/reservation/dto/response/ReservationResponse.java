package medlink.reservation.dto.response;

import lombok.*;
import medlink.reservation.entity.Reservation;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReservationResponse {
    private String reservationNo;
    private String departmentName;
    private String doctorName;
    private LocalDateTime appointmentAt;

    public static ReservationResponse from(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationNo(reservation.getReservationNo())
                .departmentName(reservation.getDoctor().getDepartment().getName())
                .doctorName(reservation.getDoctor().getName())
                .appointmentAt(reservation.getAppointmentAt())
                .build();
    }
}
