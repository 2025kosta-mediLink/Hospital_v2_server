package medlink.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus {

    // 공통
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 요청입니다."),
    NOT_READABLE(HttpStatus.BAD_REQUEST, "NOT_READABLE", "요청 본문을 해석할 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR",
            "서버 내부 오류가 발생했습니다."),

    // AUTH
    DUPLICATE_LOGIN_ID("AUTH101", "기존 사용중인 아이디입니다."),
    INVALID_LOGIN_CREDENTIALS("AUTH102", "아이디 또는 비밀번호가 일치하지 않습니다."),
    SIGNUP_FAIL("AUTH104", "회원가입에 실패했습니다."),
    NOT_FOUND_ERP_ACCOUNT("AUTH105", "계정을 찾을 수 없습니다."),

    // MEMBER
    MEMBER_NOT_FOUND("MEMBER201", "회원을 찾을 수 없습니다."),

    // DEPARTMENT
    DEPARTMENT_NOT_FOUND("DEPT301", "부서를 찾을 수 없습니다."),

    // DOCTOR
    DOCTOR_NOT_REGISTERED("DOC401", "해당 부서에 등록된 의사가 없습니다."),
    DOCTOR_NOTICE_NOT_REGISTERED("DOC402", "해당 의사나 공지 기간에 일치하는 공지가 없습니다."),
    DOCTOR_NOT_FOUND("DOC403", "의사를 찾을 수 없습니다."),
    DOCTOR_ON_EXCEPTION_DAY("DOC404", "의사의 예외 진료일에 해당합니다."),
    DOCTOR_WEEKLY_SCHEDULE_NOT_FOUND("DOC405", "의사의 진료 일정이 존재하지 않습니다."),

    // RESERVATION
    RESERVATION_DATE_OUT_OF_RANGE("RES501", "예약 가능한 날짜가 아닙니다."),
    RESERVATION_TIME_ALREADY_RESERVED("RES502", "이미 예약된 시간입니다."),
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorStatus(String code, String message) {
        this.httpStatus = HttpStatus.BAD_REQUEST; // 커스텀 에러의 상태 코드 기본값
        this.code = code;
        this.message = message;
    }

    ErrorStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
