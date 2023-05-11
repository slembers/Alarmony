package com.slembers.alarmony.alarm.dto;

import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.global.util.CommonMethods;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(builderMethodName = "alarmToAlarmDetailBuilder", access = AccessLevel.PRIVATE)
public class AlarmDetailDto {

    private Long alarmId;

    private String title;

    private int hour;

    private int minute;

    private List<Boolean> alarmDate;

    private String soundName;

    private int soundVolume;

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
