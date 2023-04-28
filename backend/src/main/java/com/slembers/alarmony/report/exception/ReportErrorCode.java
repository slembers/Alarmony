package com.slembers.alarmony.report.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ReportErrorCode implements ErrorCode {

    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "신고 기록이 존재하지 않습니다."),
    REPORT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "신고 타입이 존재하지 않습니다."),

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
