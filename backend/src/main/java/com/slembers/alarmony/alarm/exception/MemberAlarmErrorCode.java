package com.slembers.alarmony.alarm.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum MemberAlarmErrorCode implements ErrorCode {

    MEMBER_ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 멤버-알람 정보를 찾을 수 없습니다."),

    MEMBER_ALARM_INPUT_ERROR(HttpStatus.CONFLICT, "기록 등록 중 에러가 발생했습니다.")

    ;

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
