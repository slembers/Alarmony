package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.AlarmDto;
import com.slembers.alarmony.alarm.dto.response.AlarmListResponseDto;
import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.AlarmRecord;
import com.slembers.alarmony.alarm.entity.MemberAlarm;
import com.slembers.alarmony.alarm.exception.AlarmErrorCode;
import com.slembers.alarmony.alarm.exception.AlarmRecordErrorCode;
import com.slembers.alarmony.alarm.exception.MemberAlarmErrorCode;
import com.slembers.alarmony.alarm.repository.AlarmRecordRepository;
import com.slembers.alarmony.alarm.repository.AlarmRepository;
import com.slembers.alarmony.alarm.repository.MemberAlarmRepository;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.util.CommonMethods;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;

    private final MemberAlarmRepository memberAlarmRepository;

    private final MemberRepository memberRepository;

    private final AlarmRecordRepository alarmRecordRepository;

    /**
     * 유저네임을 기준으로 멤버알람 리스트를 가져오고, 이를 responseDTO에 담는다.
     *
     * @param username 아이디
     * @return 알람 리스트
     */
    @Override
    public AlarmListResponseDto getAlarmList(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        try {
            // 멤버의 멤버알람 목록을 가져온다
            List<MemberAlarm> memberAlarmList = memberAlarmRepository.findAllByMember(member);
            List<AlarmDto> alarms = new ArrayList<>();

            memberAlarmList.forEach(memberAlarm -> {
                // 각 멤버알람에 대해 연관되어 있는 알람을 가져온다.
                Alarm alarm = memberAlarm.getAlarm();
                // 중복 계산을 피하기 위해 시간을 가져온다.
                LocalTime localTime = alarm.getTime();

                // 알람 리스트를 넣을 DTO를 빌딩한다.
                AlarmDto alarmDto = AlarmDto.builder()
                        .alarmId(alarm.getId())
                        .title(alarm.getTitle())
                        .hour(localTime.getHour() / 12 == 0 ? localTime.getHour() : localTime.getHour() - 12)
                        .minute(localTime.getMinute())
                        .ampm(alarm.getTime().getHour() / 12 == 0 ? "오전" : "오후")
                        .alarmDate(CommonMethods.changeByteListToStringList(alarm.getAlarmDate()))
                        .build();

                alarms.add(alarmDto);
            });
            // 리스트를 객체에 담아서 전송한다.
            return AlarmListResponseDto.builder().alarms(alarms).build();
        } catch (Exception e) {
            throw new CustomException(AlarmErrorCode.ALARM_GET_ERROR);
        }
    }

    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     *
     * @param alarmId 알람 아이디
     * @param message 메시지
     */
    @Override
    public void putAlarmMessage(String username, Long alarmId, String message) {
        try {
            Member member = memberRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

            // 멤버 정보와 알람 아이디를 바탕으로 알람 레코드를 가져온다.
            AlarmRecord alarmRecord = alarmRecordRepository.findByMemberAndAlarm(member.getId(), alarmId)
                    .orElseThrow(() -> new CustomException(AlarmRecordErrorCode.ALARM_RECORD_NOT_EXIST));
            // 메시지를 바꾼다.
            alarmRecord.changeMessage(message);
            alarmRecordRepository.save(alarmRecord);
        } catch (Exception e) {
            throw new CustomException(MemberAlarmErrorCode.MEMBER_ALARM_INPUT_ERROR);
        }
    }

    /**
     * 특정 알람 아이디의 정보를 반환한다.
     * @param alarmId 알람 아이디
     * @return 알람 정보
     */
    @Override
    public AlarmDto getAlarmInfo(Long alarmId) {
        try {
            // 알람 정보를 가져온다.
            Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));
            // 중복 계산을 피하기 위해 시간 정보를 가져온다.
            LocalTime localTime = alarm.getTime();

            // 알람 객체를 빌딩해서 바로 리턴한다.
            return AlarmDto.builder()
                    .alarmId(alarm.getId())
                    .title(alarm.getTitle())
                    .hour(localTime.getHour() / 12 == 0 ? localTime.getHour() : localTime.getHour() - 12)
                    .minute(localTime.getMinute())
                    .ampm(alarm.getTime().getHour() / 12 == 0 ? "오전" : "오후")
                    .alarmDate(CommonMethods.changeByteListToStringList(alarm.getAlarmDate()))
                    .build();
        } catch (Exception e) {
            throw new CustomException(AlarmErrorCode.ALARM_GET_ERROR);
        }
    }
}
