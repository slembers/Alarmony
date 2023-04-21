package com.slembers.alarmony.global.redis.service.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum RedisErrorCode  implements ErrorCode {

    REDIS_ERROR_CODE(HttpStatus.INTERNAL_SERVER_ERROR,"Redis 오류 발생");

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
