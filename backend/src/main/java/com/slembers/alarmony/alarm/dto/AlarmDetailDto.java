package com.slembers.alarmony.alarm.dto;

import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.global.util.CommonMethods;
import com.slembers.alarmony.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(
        builderMethodName = "alarmToAlarmDetailBuilder",
        access = AccessLevel.PRIVATE
)
public class AlarmDetailDto {

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

    public static AlarmDetailDto builder(Alarm alarm, Member receiver) {
        return alarmToAlarmDetailBuilder()
                .host(receiver.equals(alarm.getHost()))
                .alarmId(alarm.getId())
                .title(alarm.getTitle())
                .content(alarm.getContent())
                .hour(alarm.getTime().getHour())
                .minute(alarm.getTime().getMinute())
                .alarmDate(CommonMethods.changeStringToBooleanList(alarm.getAlarmDate()))
                .soundName(alarm.getSoundName())
                .soundVolume(alarm.getSoundVolume())
                .vibrate(alarm.isVibrate())
                .build();
    }
}
