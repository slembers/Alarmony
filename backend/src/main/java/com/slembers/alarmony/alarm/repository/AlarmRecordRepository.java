package com.slembers.alarmony.alarm.repository;

import com.slembers.alarmony.alarm.entity.AlarmRecord;
import com.slembers.alarmony.alarm.entity.MemberAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlarmRecordRepository extends JpaRepository<AlarmRecord, Long> {

    /**
     * 멤버 아이디와 알람 아이디를 넘기면 해당 알람 기록 튜플을 찾아서 반환한다.
     * @param memberId
     * @param alarmId
     * @return
     */
    @Query(value = "select * from alarm_record ar join member_alarm ma on ar.member_alarm_id = ma.member_alarm_id " +
            "where ma.member_id = :memberId and ma.alarm_id = :alarmId", nativeQuery = true)
    Optional<AlarmRecord> findByMemberAndAlarm(Long memberId, Long alarmId);

    /**
     * 멤버 알람 정보를 기준으로 알람 기록을 삭제한다.
     *
     * @param memberAlarm 멤버 알람 정보
     */
    void deleteByMemberAlarm(MemberAlarm memberAlarm);

}
