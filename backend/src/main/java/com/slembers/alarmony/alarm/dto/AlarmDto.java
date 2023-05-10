package com.slembers.alarmony.alarm.dto;

import com.slembers.alarmony.global.util.CommonMethods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {

    private Long alarmId;

    private String title;

    private int hour;

    private int minute;

    private List<Boolean> alarmDate;

    /**
     *
     * @param alarmId 알람 아이디
     * @param title 제목
     * @param hour 시간
     * @param minute 분
     * @param alarmDate 알람 요일 정보
     */
    public AlarmDto(Long alarmId, String title, int hour, int minute, String alarmDate) {
        this.alarmId = alarmId;
        this.title = title;
        this.hour = hour;
        this.minute = minute;
        this.alarmDate = CommonMethods.changeStringToBooleanList(alarmDate);
    }
}
