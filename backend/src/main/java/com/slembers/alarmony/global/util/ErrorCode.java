package com.slembers.alarmony.global.util;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus getHttpStatus();
    String getDetail();
    String getName();
}
