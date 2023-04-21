package com.slembers.alarmony.member.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
public enum MemberErrorCode implements ErrorCode {

    EMAIL_DUPLICATED(HttpStatus.OK, "중복되는 이메일 입니다."),
    ID_DUPLICATED(HttpStatus.OK, "중복되는 아이디 입니다."),
    NICKNAME_DUPLICATED(HttpStatus.OK, "중복되는 닉네임 입니다.");
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

