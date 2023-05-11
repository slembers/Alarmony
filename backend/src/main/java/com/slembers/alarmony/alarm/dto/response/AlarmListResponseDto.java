package com.slembers.alarmony.alarm.dto.response;

import com.slembers.alarmony.alarm.dto.AlarmDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AlarmListResponseDto {

    private List<AlarmDto> alarms;
}
