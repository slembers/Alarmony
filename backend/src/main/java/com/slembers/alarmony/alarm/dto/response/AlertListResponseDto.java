package com.slembers.alarmony.alarm.dto.response;

import com.slembers.alarmony.alarm.dto.AlertDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AlertListResponseDto {

    private List<AlertDto> alerts;
}
