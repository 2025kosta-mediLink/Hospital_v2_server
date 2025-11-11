package medlink.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReservationRequest {
    @NotNull
    private Long doctorId;
    @NotNull
    private LocalDateTime reservationTime;
}
