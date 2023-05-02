package com.slembers.alarmony.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class AlarmEndRecordDto {

    Long alarmId;
    String username;
    LocalDateTime datetime;
    boolean success;
}
