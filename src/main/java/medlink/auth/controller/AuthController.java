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
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v2/auth", produces =
        MediaType.APPLICATION_JSON_VALUE)
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
     * 로그인 사용자 정보 추출 방식
     */
    @GetMapping("/me")
    public ApiResponse<MemberSessionResponse> me(HttpServletRequest req) {
        var user = AuthSessionUtil.getLoginUserOrNull(req);
        if (user == null) throw new GlobalException(ErrorStatus.UNAUTHORIZED);
        return ApiResponse.onSuccess(user);
    }
}