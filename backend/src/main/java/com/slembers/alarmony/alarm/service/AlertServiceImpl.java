package com.slembers.alarmony.alarm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.slembers.alarmony.alarm.dto.AlarmDetailDto;
import com.slembers.alarmony.alarm.dto.AlertDto;
import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
import com.slembers.alarmony.alarm.dto.response.AlarmInviteResponseDto;
import com.slembers.alarmony.alarm.dto.response.AlertListResponseDto;
import com.slembers.alarmony.alarm.entity.*;
import com.slembers.alarmony.alarm.exception.AlarmErrorCode;
import com.slembers.alarmony.alarm.exception.AlarmRecordErrorCode;
import com.slembers.alarmony.alarm.exception.AlertErrorCode;
import com.slembers.alarmony.alarm.exception.MemberAlarmErrorCode;
import com.slembers.alarmony.alarm.repository.AlarmRecordRepository;
import com.slembers.alarmony.alarm.repository.AlarmRepository;
import com.slembers.alarmony.alarm.repository.AlertRepository;
import com.slembers.alarmony.alarm.repository.MemberAlarmRepository;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;
    private final AlertRepository alertRepository;
    private final MemberAlarmRepository memberAlarmRepository;
    private final AlarmRecordRepository alarmRecordRepository;

    /**
     * 멤버 집합을 돌며 유효한 멤버에게 초대 알림을 보낸다.
     *
     * @param inviteMemberSetToGroupDto 그룹 초대에 필요한 dto
     */
    @Override
    public void inviteMemberToGroup(InviteMemberSetToGroupDto inviteMemberSetToGroupDto) {

        List<Member> validMemberList = inviteMemberSetToGroupDto.getNicknames().stream()
            .map(memberRepository::findByNickname)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

        Member sender = memberRepository.findByUsername(inviteMemberSetToGroupDto.getSender())
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        Alarm alarm = alarmRepository.findById(inviteMemberSetToGroupDto.getGroupId())
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        validMemberList.stream()
            .map(receiver -> Alert.builder()
                .type(AlertTypeEnum.INVITE)
                .content(String.format("'%s' 그룹 초대입니다.'", alarm.getTitle()))
                .sender(sender)
                .receiver(receiver)
                .alarm(alarm)
                .build())
            .forEach(this::sendInviteAlert);
    }

    /**
     * 알림 객체를 받아서 초대 알림을 보낸다.
     *
     * @param alert 알림 객체
     */
    @Override
    public void sendInviteAlert(Alert alert) {
        try {
            String targetMobile = alert.getReceiver().getRegistrationToken();
            // 메시지 설정
            Message message = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle("Alarmony 그룹 초대 알림")
                    .setBody(alert.getReceiver().getNickname() + "님에게 " + alert.getContent())
                    .build())
                .setToken(targetMobile)
                .build();
            // 웹 API 토큰을 가져와서 보냄
            String response = FirebaseMessaging.getInstance().send(message);
            // 결과 출력
            log.info("초대 메시지 전송 완료: " + response);

            // 푸쉬 알림을 보냈으니, 알림 테이블에도 추가해야 한다

        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_INVITE_SEND_ERROR);
        }

        try {
            alertRepository.save(alert);
        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_SERVER_ERROR);
        }
    }

    /**
     * 알림 테스트 전송 메소드
     */
    @Override
    public void testPushAlert(String username) {
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        try {
            sendMessageTo(member.getRegistrationToken(), "test", "This is Test Message");
        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_SERVER_ERROR);
        }
    }

    /**
     * 특정 유저의 알림 목록 가져오기
     *
     * @param username 아이디
     * @return 알림 목록
     */
    @Override
    public AlertListResponseDto getAlertList(String username) {
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        try {
            List<AlertDto> alertDtos = alertRepository.findMemberAlertDtos(member);
            return AlertListResponseDto.builder().alerts(alertDtos).build();
        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_NOT_FOUND);
        }
    }

    /**
     * 특정 알림을 선택하여 지울 수 있다.
     *
     * @param alertId 알림 아이디
     */
    @Override
    public void deleteAlert(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new CustomException(AlertErrorCode.ALERT_NOT_FOUND));
        try {
            alertRepository.delete(alert);
        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_DELETE_ERROR);
        }
    }

    /**
     * 초대 요청을 수락한다.
     *
     * @param alertId 알림 아이디
     */
    @Override
    public AlarmInviteResponseDto acceptInvite(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new CustomException(AlertErrorCode.ALERT_NOT_FOUND));
        // 알람 초대를 수락했으니, 멤버-알람과 알람-기록을 추가해야 한다. 이 코드 실행은 alarmservice로 넘긴다.

        MemberAlarm memberAlarm;
        // 알람-멤버에 추가한다.
        try {
            memberAlarm = MemberAlarm.builder()
                .member(alert.getReceiver())
                .alarm(alert.getAlarm())
                .build();
            memberAlarmRepository.save(memberAlarm);
        } catch (Exception e) {
            throw new CustomException(MemberAlarmErrorCode.MEMBER_ALARM_INPUT_ERROR);
        }

        // 알림-기록에 추가한다.
        try {
            AlarmRecord alarmRecord = AlarmRecord.builder()
                .memberAlarm(memberAlarm)
                .successCount(0)
                .totalCount(0)
                .message("")
                .build();
            alarmRecordRepository.save(alarmRecord);
        } catch (Exception e) {
            // 알림-기록 추가에 실패하면 알람-멤버도 지워야 한다.
            memberAlarmRepository.delete(memberAlarm);
            throw new CustomException(AlarmRecordErrorCode.ALARM_RECORD_INPUT_ERRER);
        }

        sendCustomAlert(Alert.builder()
            .sender(alert.getReceiver())
            .receiver(alert.getSender())
            .content(alert.getReceiver().getNickname() + "님이 그룹 초대를 수락하셨습니다.")
            .type(AlertTypeEnum.REPLY)
            .alarm(alert.getAlarm())
            .build(), "Alarmony 그룹 초대 수락");
        try {
            alertRepository.delete(alert);
        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_DELETE_ERROR);
        }

        return AlarmInviteResponseDto.builder()
                .alarmDetail(
                        AlarmDetailDto.builder(alert.getAlarm()))
                .message(alert.getAlarm().getTitle() + "의 그룹 초대를 수락하였습니다.")
                .build();
    }

    /**
     * 초대 요청을 거절한다.
     *
     * @param alertId 알림 아이디
     */
    @Override
    public AlarmInviteResponseDto refuseInvite(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new CustomException(AlertErrorCode.ALERT_NOT_FOUND));
        sendCustomAlert(Alert.builder()
            .sender(alert.getReceiver())
            .receiver(alert.getSender())
            .content(alert.getReceiver().getNickname() + "님이 그룹 초대를 거절하셨습니다.")
            .type(AlertTypeEnum.REPLY)
            .alarm(alert.getAlarm())
            .build(), "Alarmony 그룹 초대 거절");
        try {
            alertRepository.delete(alert);
        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_DELETE_ERROR);
        }
        return AlarmInviteResponseDto.builder().message(alert.getAlarm().getTitle() + "의 그룹 초대를 거절했습니다.").build();
    }

    /**
     * 커스텀한 알림을 전송한다.
     *
     * @param alert 알림
     * @param title 제목
     */
    @Override
    public void sendCustomAlert(Alert alert, String title) {
        try {
            String targetMobile = alert.getReceiver().getRegistrationToken();
            // 메시지 설정
            Message message = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(alert.getContent())
                    .build())
                .setToken(targetMobile)
                .build();
            // 웹 API 토큰을 가져와서 보냄
            String response = FirebaseMessaging.getInstance().send(message);
            // 결과 출력
            log.info("전달 알림: " + response);
        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_INVITE_SEND_ERROR);
        }

        // 푸쉬 알림을 보냈으니, 알림 테이블에도 추가해야 한다
        try {
            alertRepository.save(alert);
        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_SERVER_ERROR);
        }
    }

    /**
     * @param targetToken 목표 기기 토큰
     * @param title       제목
     * @param body        내용
     */
    public void sendMessageTo(String targetToken, String title, String body) {
        try {
            // 메시지 설정
            Message message = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle("Alarmony")
                    .setBody("일해라 박성완")
                    .build())
                .setToken(targetToken)
                .build();

            // 웹 API 토큰을 가져와서 보냄
            String response = FirebaseMessaging.getInstance().send(message);
            // 결과 출력
            log.info("Successfully sent message: " + response);
        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_SERVER_ERROR);
        }
    }

    /**
     * 사용자에게 알람을 보낸다.
     *
     * @param groupId  그룹 id
     * @param nickname 알람을 보낼 사용자의 닉네임
     */
    @Override
    public void sendAlarm(Long groupId, String nickname) {

        Alarm alarm = alarmRepository.findById(groupId)
            .orElseThrow(() -> new CustomException(AlertErrorCode.ALERT_NOT_FOUND));
        Member member = memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (!memberAlarmRepository.existsByMemberAndAlarm(member, alarm)) {
            throw new CustomException(MemberAlarmErrorCode.MEMBER_ALARM_NOT_FOUND);
        }
        sendAlarmTo(member.getRegistrationToken(), alarm.getTitle());
    }

    /**
     * 사용자에게 알람을 보낸다.
     *
     * @param targetToken 목표 기기 토큰
     * @param groupTitle  그룹 타이틀
     */
    private void sendAlarmTo(String targetToken, String groupTitle) {
        try {
            // 메시지 설정
            Message message = Message.builder()
                .putData("type", "ALARM")
                .putData("group", groupTitle)
                .setToken(targetToken)
                .build();

            // 웹 API 토큰을 가져와서 보냄
            String response = FirebaseMessaging.getInstance().send(message);
            // 결과 출력
            log.info("Successfully sent message: " + response);
        } catch (Exception e) {
            throw new CustomException(AlertErrorCode.ALERT_SERVER_ERROR);
        }
    }
}
