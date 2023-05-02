package com.slembers.alarmony.alarm.repository;

import com.slembers.alarmony.alarm.dto.AlarmDto;
import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.MemberAlarm;
import com.slembers.alarmony.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
     * 멤버와 알람 아이디 정보로 멤버 알람 존재 여부를 확인한다. 멤버가 알람에 속했는지 확인한다.
     *
     * @param member 멤버 정보
     * @param alarm  알람 정보
     * @return 멤버 알람 정보 존재 여부
     */
    boolean existsByMemberAndAlarm(Member member, Alarm alarm);

    /**
     * 멤버 아이디로 알람 목록을 모두 가져온다.
     * @param memberId 멤버 아이디
     * @return 알람 리스트
     */
    @Query("SELECT new com.slembers.alarmony.alarm.dto.AlarmDto( " +
            "ar.id, ar.title, hour(ar.time), minute(ar.time), ar.alarmDate) " +
            "from member_alarm as ma inner join alarm as ar on ma.alarm.id = ar.id " +
            "where ma.member.id = :memberId")
    List<AlarmDto> getAlarmDtosByMember(Long memberId);

    /**
     * 알람에 속한 멤버 수를 반환한다.
     *
     * @param alarmId 알람 id
     * @return 알람에 속한 멤버 수
     */
    int countByAlarmId(Long alarmId);

}
