package com.slembers.alarmony.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
public class CreateAlarmDto {

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "hour")
    private int hour;

    @JsonProperty(value = "minute")
    private int minute;

    @JsonProperty(value = "alarm_date")
    private List<Boolean> alarmDate;

    @JsonProperty(value = "members")
    private Set<String> members;

    @JsonProperty(value = "sound_name")
    private String soundName;

    @JsonProperty(value = "sound_volume")
    private int soundVolume;

    @JsonProperty(value = "vibrate")
    private boolean vibrate;
}
