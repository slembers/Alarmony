package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.AlarmRecordDto;
import com.slembers.alarmony.alarm.dto.AlarmEndRecordDto;
import com.slembers.alarmony.alarm.dto.MemberRankingDto;
import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.AlarmRecord;
import com.slembers.alarmony.alarm.exception.AlarmRecordErrorCode;
import com.slembers.alarmony.alarm.repository.AlarmRecordRepository;
import com.slembers.alarmony.global.execption.CustomException;
import java.time.LocalDateTime;
import java.util.List;

import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmRecordServiceImpl implements AlarmRecordService {

    private final AlarmService alarmService;
    private final AlarmRecordRepository alarmRecordRepository;
    private final MemberService memberService;

    /**
     * 오늘의 알람 기록 정보를 가져온다.
     *
     * @param groupId 그룹 id
     * @return 알람 기록
     */
    @Override
    public List<AlarmRecordDto> getTodayAlarmRecords(Long groupId, LocalDateTime todayTime) {
        // TODO : 서버와 9시간 차이나기 때문에 오늘 알람 기록 조회시에도 9시간을 빼주어야 한다. (추후 수정해야 함)
        return alarmRecordRepository.findTodayAlarmRecordsByAlarmId(groupId, todayTime.minusHours(9));
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
        Member member = memberService.findMemberByUsername(alarmEndRecordDto.getUsername());

        AlarmRecord alarmRecord = alarmRecordRepository.findByMemberAndAlarm(member.getId(),
                alarmEndRecordDto.getAlarmId())
            .orElseThrow(() -> new CustomException(AlarmRecordErrorCode.ALARM_RECORD_NOT_EXIST));

        Alarm alarm = alarmService.findAlarmByAlarmId(alarmEndRecordDto.getAlarmId());
        // 서버와 9시간 차이가 나기 때문에 9시간을 빼준다.
        try {
            if (alarmEndRecordDto.isSuccess()) {
                alarmRecord.recordSuccess(alarm.getTime(), alarmEndRecordDto.getDatetime());
            } else {
                alarmRecord.recordFailed(alarm.getTime(), alarmEndRecordDto.getDatetime());
            }
            alarmRecordRepository.save(alarmRecord);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlarmRecordErrorCode.ALARM_RECORD_RECORD_ERROR);
        }
    }

}
