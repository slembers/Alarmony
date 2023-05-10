package com.slembers.alarmony.alarm.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PutAlarmMessageRequestDto {

    @NotNull(message = "메시지는 필수입니다.")
    private String message;
}
