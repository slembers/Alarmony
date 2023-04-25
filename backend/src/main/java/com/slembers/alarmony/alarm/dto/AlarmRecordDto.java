package com.slembers.alarmony.alarm.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AlarmRecordDto {

    String nickname;
    String profileImg;
    LocalDateTime todayAlarmRecord;

}
