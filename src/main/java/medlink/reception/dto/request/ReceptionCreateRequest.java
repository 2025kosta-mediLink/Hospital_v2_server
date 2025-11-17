package medlink.reception.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class ReceptionCreateRequest {
  @NotNull
  private Long doctorId;
  @NotEmpty
  private List<@NotNull Long> symptomIds;
  @Size(max = 500) private String noteToDoctor;
  @NotNull private Boolean consentNotice;
}
