package com.slembers.alarmony.alarm.dto;

import com.slembers.alarmony.global.util.CommonMethods;
import lombok.Getter;

import java.util.List;

@Getter
public class AlarmListDetailDto {

    private boolean host;

    private Long alarmId;

    private String title;

    private String content;

    private int hour;

    private int minute;

    private List<Boolean> alarmDate;

    private String soundName;

    private int soundVolume;

    private boolean vibrate;

    public AlarmListDetailDto(boolean host ,Long alarmId, String title, String content, int hour, int minute, String alarmDate, String soundName, int soundVolume, boolean vibrate) {
        this.host = host;
        this.alarmId = alarmId;
        this.title = title;
        this.content = content;
        this.hour = hour;
        this.minute = minute;
        this.alarmDate = CommonMethods.changeStringToBooleanList(alarmDate);
        this.soundName = soundName;
        this.soundVolume = soundVolume;
        this.vibrate = vibrate;
    }
}
