package medlink.auth.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {
    @NotBlank
    @Size(max = 50)
    private String loginId;
    @NotBlank
    @Size(min = 8, max = 64)
    private String password;
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    @Size(max = 20)
    private String phone;
    @NotBlank
    @Pattern(regexp = "^[MF]$")
    private String gender;
    @NotBlank
    @Size(max = 100)
    private String address;
    @NotBlank
    @Size(max = 14)
    private String rrn;
}
