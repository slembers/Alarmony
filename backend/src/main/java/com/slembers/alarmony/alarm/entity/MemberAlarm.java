package com.slembers.alarmony.alarm.entity;

import com.slembers.alarmony.member.entity.Member;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity(name = "member_alarm")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alarm_id", nullable = false)
    private Alarm alarm;

}
