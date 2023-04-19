package com.slembers.alarmony.alarm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slembers.alarmony.alarm.dto.AlarmDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
public class AlarmListResponseDto {

    @JsonProperty(value = "alarms")
    private List<AlarmDto> alarms;
}
