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
    RESERVATION_NOT_FOUND("RES503", "예약을 찾을 수 없습니다."),
    RESERVATION_ACCESS_FORBIDDEN("RES504", "예약에 접근할 권한이 없습니다."),
    RESERVATION_NOT_REGISTERED("RES505", "등록된 예약이 없습니다."),
    RESERVATION_ACCESS_DENIED("RES506", "본인의 예약만 취소할 수 있습니다."),
    RESERVATION_CANNOT_CANCEL("RES507", "해당 상태에서는 예약 취소가 불가능합니다."),
    RESERVATION_NOT_AVAILABLE_FOR_RECEPTION("RES508", "해당 상태에서는 접수를 생성할 수 없습니다."),
    RESERVATION_NOT_TODAY("RES509", "오늘 예약 건만 접수할 수 있습니다."),

    // RECEPTION
    RECEPTION_ALREADY_EXISTS("RCN601", "해당 예약으로 이미 접수가 생성되었습니다."),
    RECEPTION_NOT_FOUND("RCN602", "접수를 찾을 수 없습니다."),
    RECEPTION_ACCESS_FORBIDDEN("RCN603", "접수에 접근할 권한이 없습니다."),
    RECEPTION_ALREADY_FINALIZED("RCN604", "이미 종료된 접수입니다."),
    RECEPTION_NO_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "RCN605", "접수 번호를 생성할 수 없습니다."),
    RECEPTION_CONSENT_REQUIRED("RCN606", "접수 진행을 위해 동의가 필요합니다."),
    RECEPTION_INVALID_SYMPTOM("RCN607", "유효하지 않은 증상 정보입니다."),

    // Waiting Ticket 관련
    WAITING_TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "WAITING_TICKET_404_1", "대기표를 찾을 수 없습니다."),
    WAITING_TICKET_NOT_WAITING(HttpStatus.BAD_REQUEST, "WAITING_TICKET_400_1", "대기 중인 상태가 아닙니다."),
    NO_WAITING_PATIENTS(HttpStatus.NOT_FOUND, "WAITING_TICKET_404_2", "대기 중인 환자가 없습니다."),
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
