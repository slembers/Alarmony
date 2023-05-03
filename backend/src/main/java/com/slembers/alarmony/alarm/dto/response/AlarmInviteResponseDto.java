package com.slembers.alarmony.alarm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slembers.alarmony.alarm.dto.AlarmDetailDto;
import lombok.Builder;

@Builder
public class AlarmInviteResponseDto {

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "alarm")
    private AlarmDetailDto alarmDetail;
}
