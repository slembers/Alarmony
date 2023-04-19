package com.slembers.alarmony.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public class AlarmDto {

    @JsonProperty(value = "alarm_id")
    private Long alarmId;

    @JsonProperty(value = "title")
    private String title;

//    @JsonProperty(value = "time")
//    private LocalTime time;

    @JsonProperty(value = "hour")
    private int hour;

    @JsonProperty(value = "minute")
    private int minute;

    @JsonProperty(value = "ampm")
    private String ampm;

    @JsonProperty(value = "alarm_date")
    private List<String> alarmDate;
}
