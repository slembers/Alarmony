package com.slembers.alarmony.member.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
public enum AuthErrorCode implements ErrorCode {
    NO_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "Authentication 정보가 존재하지 않습니다.");

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
