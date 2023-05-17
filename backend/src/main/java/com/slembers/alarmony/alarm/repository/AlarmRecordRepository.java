package com.slembers.alarmony.alarm.repository;

import com.slembers.alarmony.alarm.dto.AlarmRecordDto;
import com.slembers.alarmony.alarm.dto.MemberRankingDto;
import com.slembers.alarmony.alarm.entity.AlarmRecord;
import com.slembers.alarmony.alarm.entity.MemberAlarm;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlarmRecordRepository extends JpaRepository<AlarmRecord, Long> {

    /**
     * 멤버 아이디와 알람 아이디를 넘기면 해당 알람 기록 튜플을 찾아서 반환한다.
     *
     * @param memberId 멤버 아이디
     * @param alarmId  알람 아이디
     * @return 알람 기록 튜플
     */
    @Query(value = "select * from alarm_record ar "
        + "join member_alarm ma on ar.member_alarm_id = ma.member_alarm_id "
        + "where ma.member_id = :memberId and ma.alarm_id = :alarmId", nativeQuery = true)
    Optional<AlarmRecord> findByMemberAndAlarm(Long memberId, Long alarmId);

    /**
     * 멤버 알람 정보를 기준으로 알람 기록을 삭제한다.
     *
     * @param memberAlarm 멤버 알람 정보
     */
    void deleteByMemberAlarm(MemberAlarm memberAlarm);

    /**
     * 오늘의 알람 기록 정보를 가져온다.
     *
     * @param groupId 그룹 id
     * @return 알람 기록
     */
    @Query("SELECT new com.slembers.alarmony.alarm.dto.AlarmRecordDto(m.nickname, m.profileImgUrl, "
        + "CASE WHEN DATE(ar.todayAlarmRecord) = DATE(:todayTime) THEN true ELSE false END, "
        + "CASE WHEN ar.todayAlarmRecord IS NULL OR DATE(ar.todayAlarmRecord) <> DATE(:todayTime) "
        + "THEN ar.message ELSE NULL END) "
        + "FROM member_alarm AS ma "
        + "INNER JOIN member AS m ON ma.member.id = m.id "
        + "LEFT JOIN alarm_record AS ar ON ma.id = ar.memberAlarm.id "
        + "WHERE ma.alarm.id = :groupId")
    List<AlarmRecordDto> findTodayAlarmRecordsByAlarmId(Long groupId, LocalDateTime todayTime);

    /**
     * 알람 랭킹 기록을 얻어온다.
     *
     * @param groupId 그룹 아이디
     * @return 알람 랭킹 기록
     */
    @Query(
        "SELECT new com.slembers.alarmony.alarm.dto.MemberRankingDto(m.nickname, m.profileImgUrl, CAST(ar.totalWakeUpTime / ar.totalCount AS float)) "
            + "FROM member_alarm AS ma "
            + "INNER JOIN member AS m ON ma.member.id = m.id "
            + "INNER JOIN alarm_record AS ar ON ma.id = ar.memberAlarm.id "
            + "WHERE ma.alarm.id = :groupId "
            + "ORDER BY CAST(ar.totalWakeUpTime / ar.totalCount AS float) NULLS LAST ")
    List<MemberRankingDto> findMemberRankingsByAlarmId(Long groupId);

    /**
     * 알람 id와 일치하는 모든 알람 기록을 삭제한다.
     *
     * @param alarmId 알람 id
     */
    @Modifying
    @Query("DELETE FROM alarm_record ar WHERE ar.memberAlarm.id IN " +
        "(SELECT ma.id FROM member_alarm ma WHERE ma.alarm.id = :alarmId)")
    void deleteByAlarmId(Long alarmId);

}
