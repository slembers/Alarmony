package com.slembers.alarmony.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.global.util.CommonMethods;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.List;

@Builder(builderMethodName = "alarmToAlarmDetailBuilder", access = AccessLevel.PRIVATE)
public class AlarmDetailDto {
    @JsonProperty(value = "alarmId")
    private Long alarmId;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "hour")
    private int hour;

    @JsonProperty(value = "minute")
    private int minute;

    @JsonProperty(value = "alarmDate")
    private List<Boolean> alarmDate;

    @JsonProperty(value = "soundName")
    private String soundName;

    @JsonProperty(value = "soundVolume")
    private int soundVolume;

    @JsonProperty(value = "vibrate")
    private boolean vibrate;

    public static AlarmDetailDto builder(Alarm alarm) {
        return alarmToAlarmDetailBuilder()
                .alarmId(alarm.getId())
                .title(alarm.getTitle())
                .hour(alarm.getTime().getHour())
                .minute(alarm.getTime().getMinute())
                .alarmDate(CommonMethods.changeStringToBooleanList(alarm.getAlarmDate()))
                .soundName(alarm.getSoundName())
                .soundVolume(alarm.getSoundVolume())
                .vibrate(alarm.isVibrate())
                .build();
    }
}
