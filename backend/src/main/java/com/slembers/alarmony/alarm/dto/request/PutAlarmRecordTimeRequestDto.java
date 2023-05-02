package com.slembers.alarmony.alarm.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PutAlarmRecordTimeRequestDto {

    @JsonProperty(value = "datetime")
    private LocalDateTime datetime;
}
