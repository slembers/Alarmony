package com.slembers.alarmony.alarm.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PutAlarmMessageRequestDto {

    @JsonProperty(value = "message")
    private String message;
}
