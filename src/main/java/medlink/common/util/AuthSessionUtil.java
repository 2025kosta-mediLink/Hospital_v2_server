package medlink.common.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import medlink.auth.dto.response.MemberSessionResponse;


@NoArgsConstructor(access = AccessLevel.PRIVATE) // 인스턴스화 방지
public class AuthSessionUtil {

    // 세션에 넣을 때 쓸 키
    public static final String LOGIN_MEMBER_ATTR = "LOGIN_MEMBER";

    /**
     * 세션에 로그인 정보 저장
     * - 컨트롤러에서 이미 session.invalidate() 했다는 전제
     * - 없으면 새로 만든다
     */
    public static void setLogin(HttpServletRequest req, MemberSessionResponse sessionUser) {
        HttpSession session = req.getSession(true); // 이전 세션 없애고 새로 만들기
        session.setAttribute(LOGIN_MEMBER_ATTR, sessionUser);
    }

    /**
     * 로그인 정보 조회 (없으면 null)
     */
    public static MemberSessionResponse getLoginUserOrNull(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        Object obj = session.getAttribute(LOGIN_MEMBER_ATTR);
        return (obj instanceof MemberSessionResponse m) ? m : null;
    }

    /**
     * uuid만 필요할 때
     */
    public static String getUuidOrNull(HttpServletRequest req) {
        MemberSessionResponse user = getLoginUserOrNull(req);
        return (user == null) ? null : user.getUuid();
    }

    /**
     * 로그인 여부
     */
    public static boolean isLoggedIn(HttpServletRequest req) {
        return getLoginUserOrNull(req) != null;
    }

    /**
     * 로그아웃
     */
    public static void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
