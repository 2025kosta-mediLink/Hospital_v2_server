package medlink.auth.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import medlink.auth.dto.request.LoginRequest;
import medlink.auth.dto.request.SignUpRequest;
import medlink.auth.dto.response.MemberSessionResponse;
import medlink.auth.service.AuthService;
import medlink.common.exception.ErrorStatus;
import medlink.common.exception.GlobalException;
import medlink.common.response.ApiResponse;
import medlink.common.util.AuthSessionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 아이디 중복 체크
     */
    @GetMapping("/check-id")
    public ApiResponse<Boolean> checkId(@RequestParam String loginId) {
        boolean available = authService.isLoginIdAvailable(loginId);
        return ApiResponse.onSuccess(available);
    }

    /**
     * 회원가입
     */
    @PostMapping(value = "/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Void> signUp(@Valid @RequestBody SignUpRequest request) {
        authService.signUp(request);
        return ApiResponse.onSuccess(null);
    }

    /**
     * 로그인
     */
    @PostMapping(value = "/login")
    public ApiResponse<MemberSessionResponse> login(@Valid @RequestBody LoginRequest request,
                                                    HttpServletRequest httpReq) {
        MemberSessionResponse sessionDto = authService.login(request);

        // 세션 고정 방지
        var old = httpReq.getSession(false);
        if (old != null) old.invalidate();

        // 세션에 저장 (uuid만)
        AuthSessionUtil.setLogin(httpReq, sessionDto);

        return ApiResponse.onSuccess(null);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest req) {
        AuthSessionUtil.logout(req);
        return ApiResponse.onSuccess(null);
    }

    /**
     * 로그인 사용자 uuid 추출 방식
     */
    @GetMapping("/me")
    public ApiResponse<String> me(HttpServletRequest req) {
        String uuid = AuthSessionUtil.getUuid(req);
        if (uuid == null) throw new GlobalException(ErrorStatus.UNAUTHORIZED);
        return ApiResponse.onSuccess(uuid);
    }
}