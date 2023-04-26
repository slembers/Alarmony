package com.slembers.alarmony.alarm.exception;

import com.slembers.alarmony.global.util.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum AlarmErrorCode implements ErrorCode {

    ALARM_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 알람 정보를 찾을 수 없습니다."),
    MEMBER_NOT_IN_GROUP(HttpStatus.NOT_FOUND, "그룹에 존재하지 않는 멤버입니다."),
    ALARM_GET_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알람 정보를 가져오는 중 에러가 발생했습니다."),
    MEMBER_IN_GROUP(HttpStatus.BAD_REQUEST, "그룹장은 그룹에 멤버가 없을 때만 탈퇴 가능합니다."),
    CANNOT_REMOVE_HOST(HttpStatus.BAD_REQUEST, "그룹장은 퇴출할 수 없습니다."),
    MEMBER_NOT_HOST(HttpStatus.FORBIDDEN, "호스트 권한이 없습니다.")

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
