package com.slembers.alarmony.alarm.repository;

import com.slembers.alarmony.alarm.entity.MemberAlarm;
import com.slembers.alarmony.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberAlarmRepository extends JpaRepository<MemberAlarm, Long> {

    /**
     * 멤버를 기준으로 멤버 알람을 모두 조회한다.
     *
     * @param member
     * @return
     */
    List<MemberAlarm> findAllByMember(Member member);
}
