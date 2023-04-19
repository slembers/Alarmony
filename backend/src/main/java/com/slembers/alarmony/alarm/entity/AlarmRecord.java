package com.slembers.alarmony.alarm.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity(name = "alarm_record")
@DynamicInsert
public class AlarmRecord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_record_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_alarm_id", nullable = false)
    private MemberAlarm memberAlarm;

    @Column(name = "success_count")
    @ColumnDefault("0")
    private int successCount;

    @Column(name = "total_count")
    @ColumnDefault("0")
    private int totalCount;

    @Column(name = "today_alarm_record")
    private LocalDateTime todayAlarmRecord;

    @Column(name = "message")
    private String message;
}
