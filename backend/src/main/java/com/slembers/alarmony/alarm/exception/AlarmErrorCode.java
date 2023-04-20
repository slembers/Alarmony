package com.slembers.alarmony.alarm.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AlarmErrorCode implements ErrorCode {

    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 알람 정보를 찾을 수 없습니다")

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
