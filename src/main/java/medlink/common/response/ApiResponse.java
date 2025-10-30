package medlink.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import medlink.common.exception.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private Boolean isSuccess;
    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <T> ApiResponse<T> onSuccess(T data) {
        return new ApiResponse<>(true, String.valueOf(HttpStatus.OK.value()), null,
                data);
    }

    // 커스텀 에러 처리
    public static <T> ApiResponse<T> onFailure(ErrorStatus status) {
        return new ApiResponse<>(false, status.getCode(), status.getMessage(), null);
    }

    // RestControllerAdvice 에러 처리
    public static <T> ApiResponse<T> onFailure(ErrorStatus status, String message) {
        return new ApiResponse<>(false, status.getCode(), message, null);
    }
}
