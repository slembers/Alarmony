package com.slembers.alarmony.member.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public enum EmailVerifyErrorCode implements ErrorCode {


    EMAIL_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"내부서버에서 Email 에러가 발생했습니다."),
    VERIFY_KEY_NOT_FOUND(HttpStatus.NOT_FOUND,"해당하는 인증 키가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String detail;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public String getName() {
        return name();
    }
}
