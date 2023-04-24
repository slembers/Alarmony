package com.slembers.alarmony.alarm.repository;

import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.MemberAlarm;
import com.slembers.alarmony.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberAlarmRepository extends JpaRepository<MemberAlarm, Long> {

    /**
     * 멤버를 기준으로 멤버 알람을 모두 조회한다.
     *
     * @param member 멤버 정보
     * @return 멤버의 알람 정보
     */
    List<MemberAlarm> findAllByMember(Member member);

    /**
     * 멤버와 알람 아이디 정보로 멤버 알람을 조회한다.
     *
     * @param member 멤버 정보
     * @param alarm 알람 정보
     * @return 멤버 알람 정보를 optional로 반환
     */
    Optional<MemberAlarm> findByMemberAndAlarm(Member member, Alarm alarm);

    /**
     * 알람에 속한 멤버 수를 반환한다.
     *
     * @param alarmId 알람 id
     * @return 알람에 속한 멤버 수
     */
    int countByAlarmId(Long alarmId);

}
