package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.AlarmRecordDto;
import com.slembers.alarmony.alarm.dto.AlarmEndRecordDto;
import com.slembers.alarmony.alarm.dto.MemberRankingDto;
import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.AlarmRecord;
import com.slembers.alarmony.alarm.exception.AlarmErrorCode;
import com.slembers.alarmony.alarm.exception.AlarmRecordErrorCode;
import com.slembers.alarmony.alarm.repository.AlarmRecordRepository;
import com.slembers.alarmony.alarm.repository.AlarmRepository;
import com.slembers.alarmony.global.execption.CustomException;
import java.util.List;

import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmRecordServiceImpl implements AlarmRecordService {

    private final AlarmRepository alarmRepository;
    private final AlarmRecordRepository alarmRecordRepository;
    private final MemberRepository memberRepository;

    /**
     * 오늘의 알람 기록 정보를 가져온다.
     *
     * @param groupId 그룹 id
     * @return 알람 기록
     */
    @Override
    public List<AlarmRecordDto> getTodayAlarmRecords(Long groupId) {
        return alarmRecordRepository.findTodayAlarmRecordsByAlarmId(groupId);
    }

    /**
     * 알람 랭킹 기록을 얻어온다.
     *
     * @param groupId 그룹 id
     * @return 알람 랭킹 기록
     */
    @Override
    public List<MemberRankingDto> getAlarmRanking(Long groupId) {
        return alarmRecordRepository.findMemberRankingsByAlarmId(groupId);
    }

    /**
     * 알람 종료에 성공여부에 따라 기록한다.
     *
     * @param alarmEndRecordDto 알람 종료 객체
     */
    @Override
    public void putAlarmRecord(AlarmEndRecordDto alarmEndRecordDto) {
        Member member = memberRepository.findByUsername(alarmEndRecordDto.getUsername())
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        AlarmRecord alarmRecord = alarmRecordRepository.findByMemberAndAlarm(member.getId(),
                alarmEndRecordDto.getAlarmId())
            .orElseThrow(() -> new CustomException(AlarmRecordErrorCode.ALARM_RECORD_NOT_EXIST));

        Alarm alarm = alarmRepository.findById(alarmEndRecordDto.getAlarmId())
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        try {
            if (alarmEndRecordDto.isSuccess()) {
                alarmRecord.recordSuccess(alarm.getTime(), alarmEndRecordDto.getDatetime());
            } else {
                alarmRecord.recordFailed(alarm.getTime());
            }
            alarmRecordRepository.save(alarmRecord);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlarmRecordErrorCode.ALARM_RECORD_RECORD_ERROR);
        }
    }

}
