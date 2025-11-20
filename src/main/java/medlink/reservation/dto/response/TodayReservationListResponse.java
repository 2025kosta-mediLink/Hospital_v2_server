package medlink.reservation.dto.response;

import lombok.*;
import medlink.reservation.entity.Reservation;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TodayReservationListResponse {
    private long reservationId;
    private String reservationNo;
    private String departmentName;
    private long doctorId;
    private String doctorName;
    private String reservationTime;

    public static TodayReservationListResponse from(Reservation reservation) {
        return TodayReservationListResponse.builder()
                .reservationId(reservation.getReservationId())
                .reservationNo(reservation.getReservationNo())
                .doctorId(reservation.getDoctor().getDoctorId())
                .departmentName(reservation.getDoctor().getDepartment().getName())
                .doctorName(reservation.getDoctor().getName())
                .reservationTime(
                        reservation.getAppointmentAt()
                                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                )
                .build();
    }
}
