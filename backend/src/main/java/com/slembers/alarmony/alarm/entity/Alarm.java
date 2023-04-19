package com.slembers.alarmony.alarm.entity;

import com.slembers.alarmony.member.entity.Member;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalTime;

@Getter
@Entity(name = "alarm")
@DynamicInsert
public class Alarm {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "host_id", nullable = false)
    private Member host;

    @Column(name = "alarm_date",columnDefinition = "BINARY(7)")
    @ColumnDefault("0x00000000000000")
    private Byte[] alarmDate;

    @Column(name = "sound_name", nullable = false)
    private String soundName;

    @Column(name = "sound_volume")
    @ColumnDefault("7")
    private int soundVolume;

    @Column(name = "vibrate")
    @ColumnDefault("false")
    private boolean vibrate;

}
