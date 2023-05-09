package com.slembers.alarmony.alarm.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class CreateAlarmDto {

    private String title;

    private int hour;

    private int minute;

    private List<Boolean> alarmDate;

    private String soundName;

    private int soundVolume;

    private boolean vibrate;
}
