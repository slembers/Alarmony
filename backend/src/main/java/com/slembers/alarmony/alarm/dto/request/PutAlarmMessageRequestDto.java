package com.slembers.alarmony.alarm.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PutAlarmMessageRequestDto {

    @NotNull(message = "메시지는 필수입니다.")
    @JsonProperty(value = "message")
    private String message;
}
