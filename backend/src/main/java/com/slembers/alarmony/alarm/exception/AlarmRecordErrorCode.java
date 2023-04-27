package com.slembers.alarmony.alarm.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AlarmRecordErrorCode implements ErrorCode {

    ALARM_RECORD_RECORD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알람 기록 중 에러가 발생했습니다."),
    ALARM_RECORD_INPUT_ERRER(HttpStatus.INTERNAL_SERVER_ERROR, "알람 기록 생성 중 에러가 발생했습니다."),
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
