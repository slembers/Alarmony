package com.slembers.alarmony.alarm.dto.response;

import com.slembers.alarmony.alarm.dto.AlarmDetailDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmInviteResponseDto {

    private String message;

    private AlarmDetailDto alarmDetail;
}
