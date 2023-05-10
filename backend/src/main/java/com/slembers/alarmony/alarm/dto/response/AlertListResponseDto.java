package com.slembers.alarmony.alarm.dto.response;

import com.slembers.alarmony.alarm.dto.AlertDto;
import lombok.Builder;

import java.util.List;

@Builder
public class AlertListResponseDto {

    private List<AlertDto> alerts;
}
