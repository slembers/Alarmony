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
     * @param username
     * @return
     */
    @Override
    public AlarmListResponseDto getAlarmList(String username) {
        Member member = memberRepository.findByUsername(username)
                // 없으면 멤버 없음 예외 던지는 코드로 바꿀 것
                .orElseThrow(() -> new RuntimeException());

        try {
            List<MemberAlarm> memberAlarmList = memberAlarmRepository.findAllByMember(member);
            List<AlarmDto> alarms = new ArrayList<>();

            memberAlarmList.forEach(memberAlarm -> {
                Alarm alarm = memberAlarm.getAlarm();
                LocalTime localTime = alarm.getTime();


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

            return AlarmListResponseDto.builder().alarms(alarms).build();
        } catch (Exception e) {
            throw new CustomException(AlarmErrorCode.ALARM_GET_ERROR);
        }
    }

    /**
     * 알람 id를 사용하여 데이터베이스에서 알람 객체를 얻어온다. 알람 정보가 존재하지 않을 경우 예외를 던진다.
     *
     * @param id
     * @return 알람 객체
     */
    @Override
    public Alarm getAlarmByAlarmId(Long id) {
        return alarmRepository.findById(id)
                .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));
    }

    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     *
     * @param alarmId
     * @param message
     */
    @Override
    public void putAlarmMessage(String username, Long alarmId, String message) {
        try {
            Member member = memberRepository.findByUsername(username)
                    //TODO : 멤버 예외 Enum으로 바꾸기
                    .orElseThrow(() -> new RuntimeException());

            AlarmRecord alarmRecord = alarmRecordRepository.findByMemberAndAlarm(member.getId(), alarmId)
                    .orElseThrow(() -> new CustomException(AlarmRecordErrorCode.ALARM_RECORD_NOT_EXIST));

            alarmRecord.changeMessage(message);

            alarmRecordRepository.save(alarmRecord);
        } catch (Exception e) {
            throw new CustomException(MemberAlarmErrorCode.MEMBER_ALARM_INPUT_ERROR);
        }

    }
}
