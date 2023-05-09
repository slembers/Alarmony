package com.slembers.alarmony.alarm.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PutAlarmRecordTimeRequestDto {

    @NotNull(message = "시간은 필수입니다.")
    @JsonProperty(value = "datetime")
    private LocalDateTime datetime;
}
