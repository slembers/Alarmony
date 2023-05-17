package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.AlarmEndRecordDto;
import com.slembers.alarmony.alarm.dto.AlarmRecordDto;
import com.slembers.alarmony.alarm.dto.MemberRankingDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmRecordService {

    /**
     * 오늘의 알람 기록을 얻어온다.
     *
     * @param groupId 그룹 id
     * @return 오늘 알람 기록
     */
    List<AlarmRecordDto> getTodayAlarmRecords(Long groupId, LocalDateTime todayTime);

    /**
     * 알람 랭킹 기록을 얻어온다.
     *
     * @param groupId 그룹 id
     * @return 알람 랭킹 기록
     */
    List<MemberRankingDto> getAlarmRanking(Long groupId);

    /**
     * 알람 종료 성공 시 기록한다.
     * @param alarmEndRecordDto 알람 성공 객체
     */
    void putAlarmRecord(AlarmEndRecordDto alarmEndRecordDto);
}
