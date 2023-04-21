package com.slembers.alarmony.alarm.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AlarmRecordErrorCode implements ErrorCode {

    ALARM_RECORD_NOT_EXIST(HttpStatus.NOT_FOUND, "알람 기록이 존재하지 않습니다.")
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
