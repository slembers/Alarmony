package com.slembers.alarmony.alarm.dto;

import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class CreateAlarmDto {

    private String title;

    private int hour;

    private int minute;

    private List<Boolean> alarmDate;

    private Set<String> members;

    private String soundName;

    private int soundVolume;

    private boolean vibrate;
}
