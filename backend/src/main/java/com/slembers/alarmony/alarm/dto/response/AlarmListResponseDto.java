package com.slembers.alarmony.alarm.dto.response;

import com.slembers.alarmony.alarm.dto.AlarmDto;
import lombok.Builder;

import java.util.List;

@Builder
public class AlarmListResponseDto {

    private List<AlarmDto> alarms;
}
