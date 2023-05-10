package com.slembers.alarmony.alarm.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlarmDto {

    @NotNull(message = "제목은 필수입니다.")
    private String title;

    @Min(value = 0, message = "시간은 0 이상이어야 합니다.")
    @Max(value = 23, message = "시간은 23 이하여야 합니다.")
    private int hour;

    @Min(value = 0, message = "분은 0 이상이어야 합니다.")
    @Max(value = 59, message = "분은 59 이하여야 합니다.")
    private int minute;

    @NotNull(message = "알람 날짜 정보는 필수입니다.")
    @Size(min = 7, max = 7, message = "알람 날짜 배열의 길이는 7개여야 합니다.")
    private List<Boolean> alarmDate;

    @NotNull(message = "소리 정보는 필수입니다.")
    private String soundName;

    @Min(value = 0, message = "소리의 크기는 0 이상이어야 합니다.")
    @Max(value = 15, message = "소리의 크리는 15 이하여야 합니다.")
    private int soundVolume;

    private boolean vibrate;
}
