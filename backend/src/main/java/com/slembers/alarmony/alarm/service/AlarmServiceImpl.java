package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.AlarmDto;
import com.slembers.alarmony.alarm.dto.CreateAlarmDto;
import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
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
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;

    private final MemberAlarmRepository memberAlarmRepository;

    private final MemberRepository memberRepository;

    private final AlarmRecordRepository alarmRecordRepository;

    private final AlertService alertService;

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
            //멤버의 멤버알람 목록을 가져온다
            List<AlarmDto> alarms = memberAlarmRepository.getAlarmDtosByMember(member.getId());
            // 리스트를 객체에 담아서 전송한다.
            return AlarmListResponseDto.builder().alarms(alarms).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlarmErrorCode.ALARM_GET_ERROR);
        }
    }

    /**
     * 신규 알람을 생성한다.
     * @param username 현재 로그인 아이디
     * @param createAlarmDto 알람 생성 정보
     */
    @Override
    public Long createAlarm(String username, CreateAlarmDto createAlarmDto) {
        Member groupLeader = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        Alarm alarm;
        // 알람을 생성한다
        try {
            // 알람을 생성한다.
            alarm = Alarm.builder()
                    .title(createAlarmDto.getTitle())
                    .time(LocalTime.of(createAlarmDto.getHour(), createAlarmDto.getMinute()))
                    .host(groupLeader)
                    .alarmDate(CommonMethods.changeBooleanListToString(createAlarmDto.getAlarmDate()))
                    .soundName(createAlarmDto.getSoundName())
                    .soundVolume(createAlarmDto.getSoundVolume())
                    .vibrate(createAlarmDto.isVibrate())
                    .build();
            alarmRepository.save(alarm);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlarmErrorCode.ALARM_CREATE_ERROR);
        }

        MemberAlarm memberAlarm;
        // 그룹장을 알람-멤버에 추가한다.
        try {
            memberAlarm = MemberAlarm.builder()
                    .member(groupLeader)
                    .alarm(alarm)
                    .build();
            memberAlarmRepository.save(memberAlarm);
        } catch (Exception e) {
            log.error(e.getMessage());
            // 알람-멤버에 추가하는 도중 에러가 생긴다면 알람도 지워야 한다.
            alarmRepository.delete(alarm);
            throw new CustomException(MemberAlarmErrorCode.MEMBER_ALARM_INPUT_ERROR);
        }

        // 그룹장의 알람을 알림-기록에 추가한다.
        AlarmRecord alarmRecord;
        try {
            alarmRecord = AlarmRecord.builder()
                    .memberAlarm(memberAlarm)
                    .successCount(0)
                    .totalCount(0)
                    .message("")
                    .build();
            alarmRecordRepository.save(alarmRecord);
        } catch (Exception e) {
            log.error(e.getMessage());
            // 알림-기록 추가에 실패하면 알람과 알람-멤버도 지워야 한다.
            memberAlarmRepository.delete(memberAlarm);
            alarmRepository.delete(alarm);
            throw new CustomException(AlarmRecordErrorCode.ALARM_RECORD_INPUT_ERRER);
        }

        return alarm.getId();
    }

    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     *
     * @param alarmId 알람 아이디
     * @param message 메시지
     */
    @Override
    public void putAlarmMessage(String username, Long alarmId, String message) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        if( message == null ) throw new CustomException(AlarmRecordErrorCode.ALARM_RECORD_MESSAGE_WRONG);

        // 멤버 정보와 알람 아이디를 바탕으로 알람 레코드를 가져온다.
        AlarmRecord alarmRecord = alarmRecordRepository.findByMemberAndAlarm(member.getId(), alarmId)
                .orElseThrow(() -> new CustomException(AlarmRecordErrorCode.ALARM_RECORD_NOT_EXIST));

        try {
            alarmRecord.changeMessage(message);
            alarmRecordRepository.save(alarmRecord);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(MemberAlarmErrorCode.MEMBER_ALARM_INPUT_ERROR);
        }
    }

    /**
     * 특정 알람 아이디의 정보를 반환한다.
     *
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

            // 알람 객체를 바로 리턴한다.
            return new AlarmDto(alarm.getId(), alarm.getTitle(), localTime.getHour(), localTime.getMinute(), alarm.getAlarmDate());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlarmErrorCode.ALARM_GET_ERROR);
        }
    }
}
