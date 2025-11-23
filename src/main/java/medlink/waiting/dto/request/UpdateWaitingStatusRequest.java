package medlink.waiting.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UpdateWaitingStatusRequest {
  @NotBlank
  private String status; // "IN_SERVICE", "DONE", "CANCELLED" 등
}