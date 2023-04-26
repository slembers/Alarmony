package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.AlarmRecordDto;
import com.slembers.alarmony.alarm.dto.MemberRankingDto;
import com.slembers.alarmony.alarm.dto.response.AlarmRecordResponseDto;
import com.slembers.alarmony.alarm.repository.AlarmRecordRepository;
import com.slembers.alarmony.member.dto.MemberInfoDto;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmRecordServiceImpl implements AlarmRecordService {

    private final ModelMapper modelMapper;
    private final AlarmRecordRepository alarmRecordRepository;

    /**
     * 오늘의 알람 기록 정보를 가져온다.
     *
     * @param groupId 그룹 id
     * @return 알람 기록
     */
    @Override
    public AlarmRecordResponseDto getTodayAlarmRecords(Long groupId) {
        List<AlarmRecordDto> alarmRecordList =
            alarmRecordRepository.findTodayAlarmRecordsByAlarmId(groupId);

        LocalDate today = LocalDate.now();
        List<MemberInfoDto> alarmOn = alarmRecordList.stream()
            .filter(alarmRecord -> alarmRecord.getTodayAlarmRecord().toLocalDate().equals(today))
            .map(alarmRecord -> modelMapper.map(alarmRecord, MemberInfoDto.class))
            .collect(Collectors.toList());
        List<MemberInfoDto> alarmOff = alarmRecordList.stream()
            .filter(alarmRecord -> !alarmRecord.getTodayAlarmRecord().toLocalDate().equals(today))
            .map(alarmRecord -> modelMapper.map(alarmRecord, MemberInfoDto.class))
            .collect(Collectors.toList());

        return AlarmRecordResponseDto.builder()
            .alarmOn(alarmOn)
            .alarmOff(alarmOff)
            .build();
    }

    /**
     * 알람 랭킹 기록을 얻어온다.
     *
     * @param groupId 그룹 id
     * @return 알람 랭킹 기록
     */
    @Override
    public List<MemberRankingDto> getAlarmRanking(Long groupId) {
        List<MemberRankingDto> alarmRanking = alarmRecordRepository.findMemberRankingsByAlarmId(
            groupId);
        return alarmRanking.stream()
            .sorted(Comparator.comparing(MemberRankingDto::getWakeUpAvg,
                Comparator.nullsLast(Float::compareTo)))
            .collect(Collectors.toList());
    }

}
