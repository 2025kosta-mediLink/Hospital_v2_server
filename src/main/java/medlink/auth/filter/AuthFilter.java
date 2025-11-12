package medlink.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import medlink.common.exception.ErrorStatus;
import medlink.common.response.ApiResponse;
import medlink.common.util.AuthSessionUtil;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        // "/api/v2/auth"로 시작하는 URL은 필터를 적용하지 않음
        if (requestURI.startsWith("/api/v2/auth")) {
            chain.doFilter(request, response);
            return;
        }

        // 로그인 안 되어 있으면 에러 처리
        if (AuthSessionUtil.getLoginUserOrNull(httpRequest) == null) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            // 커스텀 응답 객체 생성
            ApiResponse<Void> apiResponse = ApiResponse.onFailure(ErrorStatus.UNAUTHORIZED);
            // 응답을 JSON 형태로 작성
            String jsonResponse = new ObjectMapper().writeValueAsString(apiResponse);
            httpResponse.getWriter().write(jsonResponse);
            return;  // 더 이상 진행되지 않도록 처리
        }

        chain.doFilter(request, response); // 로그인 되어 있으면 다음 필터로 요청 전달
    }
}
