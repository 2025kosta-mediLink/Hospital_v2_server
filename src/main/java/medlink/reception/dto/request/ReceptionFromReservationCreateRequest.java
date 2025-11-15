package medlink.reception.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ReceptionFromReservationCreateRequest {

  @NotNull
  private Long reservationId;

  @NotEmpty
  private List<Long> symptomIds;

  private String noteToDoctor;

  @NotNull
  private Boolean consentNotice;
}
