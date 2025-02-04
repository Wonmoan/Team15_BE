package kakao.rebit.member.exception;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum MemberErrorCode implements ErrorCode {
    NOT_FOUND("M001", HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    NOT_ENOUGH_POINTS("M002", HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    MemberErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
