package com.slembers.alarmony.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slembers.alarmony.global.util.CommonMethods;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class AlarmDto {

    @JsonProperty(value = "alarm_id")
    private Long alarmId;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "hour")
    private int hour;

    @JsonProperty(value = "minute")
    private int minute;

    @JsonProperty(value = "alarm_date")
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
