package medlink.common.exception;

import medlink.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 비즈니스 예외 처리 (커스텀 4xx 등)
     */
    @ExceptionHandler(GlobalException.class)
    public ApiResponse<Void> handleGlobalException(
            GlobalException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        ErrorStatus es = ex.getStatus();
        response.setStatus(es.getHttpStatus().value());

        log.warn("code: {}, uri: {}, msg: {}", es.name(), request.getRequestURI(),
                ex.getMessage() != null ? ex.getMessage() : es.getMessage());
        return ApiResponse.onFailure(es, ex.getMessage() != null ? ex.getMessage() : es.getMessage());
    }

    /**
     * @Valid @RequestBody 검증 실패 → 400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        log.warn("uri: {}, fields: {}", request.getRequestURI(), message);
        return ApiResponse.onFailure(ErrorStatus.BAD_REQUEST, message);
    }

    /**
     * @Valid @ModelAttribute, @RequestParam 검증 실패 → 400
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse<Void> handleBindException(
            BindException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        log.warn("uri: {}, fields: {}", request.getRequestURI(), message);
        return ApiResponse.onFailure(ErrorStatus.BAD_REQUEST, message);
    }

    /**
     * JSON 파싱 실패 / 요청 본문 읽기 실패 → 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        log.warn("uri: {}, error: {}", request.getRequestURI(), ex.getMessage());
        return ApiResponse.onFailure(ErrorStatus.NOT_READABLE);
    }

    /**
     * 예상치 못한 모든 런타임 예외 처리 (내부 메시지 숨김)
     */
    @ExceptionHandler(RuntimeException.class)
    protected ApiResponse<Void> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (ex instanceof GlobalException ge) {
            return handleGlobalException(ge, request, response);
        }

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.warn("uri: {}, error: {}", request.getRequestURI(), ex.getMessage(), ex);
        return ApiResponse.onFailure(
                ErrorStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage() != null ? ex.getMessage() : "서버 오류"
        );
    }
}

