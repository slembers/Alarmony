package com.slembers.alarmony.alarm.entity;

import com.slembers.alarmony.alarm.dto.AlarmInfoDto;
import com.slembers.alarmony.global.util.CommonMethods;
import com.slembers.alarmony.member.entity.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalTime;

@Entity(name = "alarm")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Member host;

    @Column(name = "alarm_date", columnDefinition = "VARCHAR(7)")
    @ColumnDefault("'0000000'")
    private String alarmDate;

    @Column(name = "sound_name", nullable = false)
    private String soundName;

    @Column(name = "sound_volume")
    @ColumnDefault("7")
    private int soundVolume;

    @Column(name = "vibrate")
    @ColumnDefault("false")
    private boolean vibrate;

    public void changeHost(Member member) {
        this.host = member;
    }

    public void changeInfo(AlarmInfoDto infoDto) {
        this.title = infoDto.getTitle();
        this.content = infoDto.getContent();
        this.time = LocalTime.of(infoDto.getHour(), infoDto.getMinute());
        this.alarmDate = CommonMethods.changeBooleanListToString(infoDto.getAlarmDate());
        this.soundName = infoDto.getSoundName();
        this.soundVolume = infoDto.getSoundVolume();
        this.vibrate = infoDto.isVibrate();
    }
}
