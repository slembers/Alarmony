package com.slembers.alarmony.alarm.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PutAlarmRecordTimeRequestDto {

    @NotNull(message = "시간은 필수입니다.")
    private LocalDateTime datetime;
}
