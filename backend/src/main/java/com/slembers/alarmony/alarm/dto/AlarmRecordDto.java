package com.slembers.alarmony.alarm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Null 값인 필드 제외
public class AlarmRecordDto {

    String nickname;
    String profileImg;
    boolean success;
    String message;

}
