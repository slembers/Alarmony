package com.slembers.alarmony.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AlarmRecordDto {

    String nickname;
    String profileImg;
    boolean success;

}
