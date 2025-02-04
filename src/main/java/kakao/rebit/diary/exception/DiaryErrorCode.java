package kakao.rebit.diary.exception;

import kakao.rebit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum DiaryErrorCode implements ErrorCode {
    DIARY_NOT_FOUND("D001", HttpStatus.NOT_FOUND, "해당 독서 일기를 찾을 수 없습니다."),
    DIARY_VALIDATION_FAILED("D002", HttpStatus.BAD_REQUEST, "다이어리 데이터 검증에 실패하였습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    DiaryErrorCode(String code, HttpStatus httpStatus, String message) {
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
