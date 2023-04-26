package com.slembers.alarmony.alarm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slembers.alarmony.alarm.dto.AlertDto;
import lombok.Builder;

import java.util.List;

@Builder
public class AlertListResponseDto {

    @JsonProperty(value = "alerts")
    private List<AlertDto> alerts;
}
