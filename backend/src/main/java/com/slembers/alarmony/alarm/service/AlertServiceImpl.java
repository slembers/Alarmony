package com.slembers.alarmony.alarm.service;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidConfig.Priority;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.slembers.alarmony.alarm.dto.AlarmDetailDto;
import com.slembers.alarmony.alarm.dto.AlertDto;
import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
import com.slembers.alarmony.alarm.dto.response.AlarmInviteResponseDto;
import com.slembers.alarmony.alarm.dto.response.AlertListResponseDto;
import com.slembers.alarmony.alarm.dto.response.AutoLogoutValidDto;
import com.slembers.alarmony.alarm.entity.*;
import com.slembers.alarmony.alarm.exception.AlarmRecordErrorCode;
import com.slembers.alarmony.alarm.exception.AlertErrorCode;
import com.slembers.alarmony.alarm.exception.MemberAlarmErrorCode;
import com.slembers.alarmony.alarm.repository.AlarmRecordRepository;
import com.slembers.alarmony.alarm.repository.AlertRepository;
import com.slembers.alarmony.alarm.repository.MemberAlarmRepository;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.security.util.SecurityUtil;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;

import java.util.List;

import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final MemberRepository memberRepository;
    private final AlertRepository alertRepository;
    private final AlarmService alarmService;
    private final MemberAlarmRepository memberAlarmRepository;
    private final AlarmRecordRepository alarmRecordRepository;

    /**
     * 멤버 집합을 돌며 유효한 멤버에게 초대 알림을 보낸다.
     *
     * @param inviteMemberSetToGroupDto 그룹 초대에 필요한 dto
     */
    @Override
    public int inviteMemberToGroup(InviteMemberSetToGroupDto inviteMemberSetToGroupDto) {

        Member sender = memberRepository.findByUsername(inviteMemberSetToGroupDto.getSender())
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        Alarm alarm = alarmService.findAlarmByAlarmId(inviteMemberSetToGroupDto.getGroupId());

        return inviteMemberSetToGroupDto.getNicknames().stream()
            .map(memberRepository::findByNickname)
            .flatMap(Optional::stream)
            .filter(member -> !memberAlarmRepository.existsByMemberAndAlarm(member, alarm))
            .map(receiver -> createAlert(receiver, sender, alarm, AlertTypeEnum.INVITE))
            .filter(this::sendAlert)
            .mapToInt(result -> 1)
            .sum();
    }

    /**
     * 유저네임을 돌며 그룹 삭제 메시지를 보낸다.
     *
     * @param groupId      그룹 id
     * @param usernameList 유저네임 리스트
     */
    @Override
    public void removeMemberFromGroup(Long groupId, List<String> usernameList) {

        Alarm alarm = alarmService.findAlarmByAlarmId(groupId);

        usernameList.stream()
            .map(memberRepository::findByUsername)
            .flatMap(Optional::stream)
            .map(receiver -> createAlert(receiver, null, alarm, AlertTypeEnum.DELETE))
            .forEach(this::sendAlert);
    }

    /**
     * 그룹 퇴출 알림을 보낸다.
     *
     * @param groupId  그룹 id
     * @param nickname 닉네임
     */
    @Transactional
    public void sendBanAlert(Long groupId, String nickname) {

        Alarm alarm = alarmService.findAlarmByAlarmId(groupId);
        Member receiver = memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        sendAlert(createAlert(receiver, null, alarm, AlertTypeEnum.BANN));
    }

    private Alert createAlert(Member receiver, Member sender, Alarm alarm, AlertTypeEnum type) {
        String content;
        if (type == AlertTypeEnum.INVITE) {
            content = String.format("'%s' 그룹 초대입니다.", alarm.getTitle());
        } else if (type == AlertTypeEnum.DELETE) {
            content = String.format("'%s' 그룹이 삭제되었습니다.", alarm.getTitle());
        } else if (type == AlertTypeEnum.BANN) {
            content = String.format("'%s' 그룹에서 퇴출되었습니다.", alarm.getTitle());
        } else {
            throw new CustomException(AlertErrorCode.ENUM_NOT_ALLOW);
        }
        return Alert.builder()
            .type(type)
            .content(content)
            .sender(sender)
            .receiver(receiver)
            .alarm(alarm)
            .build();
    }

    /**
     * 알림 객체를 받아서 알림을 보낸다.
     *
     * @param alert 알림 객체
     */
    @Transactional
    public boolean sendAlert(Alert alert) {
        try {
            alertRepository.save(alert);
            String targetMobile = alert.getReceiver().getRegistrationToken();
            String content = alert.getContent();
            String imageUrl = alert.getSender() == null ? "" : alert.getSender().getProfileImgUrl();
            AndroidConfig config = AndroidConfig.builder()
                .setPriority(Priority.HIGH)
                .build();
            // 메시지 설정
            Message message = Message.builder()
                .putData("alertId", String.valueOf(alert.getId()))
                .putData("alarmId", String.valueOf(alert.getAlarm().getId()))
                .putData("profileImg", imageUrl == null ? "" : imageUrl)
                .putData("content", content)
                .putData("type", alert.getType().name())
                .setToken(targetMobile)
                .setAndroidConfig(config)
                .build();
            // 웹 API 토큰을 가져와서 보냄
            String response = FirebaseMessaging.getInstance().send(message);
            // 결과 출력
            log.info("메시지 전송 완료: " + response);
            if (alert.getType().equals(AlertTypeEnum.DELETE)) {
                alert.setAlarm(null);
                alertRepository.save(alert);
            }
            // 알림 메시지를 저장한다.
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
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
            log.error(e.getMessage());
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
        confirmAlertReceiver(alert);
        try {
            alertRepository.delete(alert);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlertErrorCode.ALERT_DELETE_ERROR);
        }
    }

    /**
     * 초대 요청을 수락한다.
     *
     * @param alertId 알림 아이디
     */
    @Transactional
    @Override
    public AlarmInviteResponseDto acceptInvite(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new CustomException(AlertErrorCode.ALERT_NOT_FOUND));
        confirmAlertReceiver(alert);
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
            log.error(e.getMessage());
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
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            throw new CustomException(AlertErrorCode.ALERT_DELETE_ERROR);
        }

        return AlarmInviteResponseDto.builder()
            .alarm(
                AlarmDetailDto.builder(alert.getAlarm(), alert.getReceiver()))
            .message(alert.getAlarm().getTitle() + "의 그룹 초대를 수락하였습니다.")
            .build();
    }

    /**
     * 초대 요청을 거절한다.
     *
     * @param alertId 알림 아이디
     */
    @Transactional
    @Override
    public AlarmInviteResponseDto refuseInvite(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
            .orElseThrow(() -> new CustomException(AlertErrorCode.ALERT_NOT_FOUND));
        confirmAlertReceiver(alert);
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
            log.error(e.getMessage());
            throw new CustomException(AlertErrorCode.ALERT_DELETE_ERROR);
        }
        return AlarmInviteResponseDto.builder()
            .message(alert.getAlarm().getTitle() + "의 그룹 초대를 거절했습니다.").build();
    }

    /**
     * 커스텀한 알림을 전송한다.
     *
     * @param alert 알림
     * @param title 제목
     */
    @Transactional
    @Override
    public void sendCustomAlert(Alert alert, String title) {
        // 알림 테이블에도 추가
        try {
            alertRepository.save(alert);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlertErrorCode.ALERT_SERVER_ERROR);
        }

        try {
            String targetMobile = alert.getReceiver().getRegistrationToken();
            String imageUrl = alert.getSender() == null ? "" : alert.getSender().getProfileImgUrl();
            // 메시지 설정
            Message message = Message.builder()
                .putData("alertId", String.valueOf(alert.getId()))
                .putData("profileImg", imageUrl == null ? "" : imageUrl)
                .putData("content", alert.getContent())
                .putData("type", alert.getType().name())
                .setToken(targetMobile)
                .build();
            // 웹 API 토큰을 가져와서 보냄
            String response = FirebaseMessaging.getInstance().send(message);
            // 결과 출력
            log.info("전달 알림: " + response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlertErrorCode.ALERT_INVITE_SEND_ERROR);
        }
    }

    /**
     * 새로 로그인 할 때 원래 기기에 로그아웃 처리를 요청한다.
     * @param username 유저이름
     * @param token 등록트큰
     * @return 성공 여부
     */
    @Override
    public AutoLogoutValidDto sendAutoLogoutAlert(String username, String token) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        log.info(member.getRegistrationToken());
        log.info(token);
        if(member.getRegistrationToken().isEmpty() || member.getRegistrationToken().equals(token)) {
            return AutoLogoutValidDto.builder().success(false).build();
        }

        try {
            AndroidConfig config = AndroidConfig.builder()
                    .setPriority(Priority.HIGH)
                    .build();

            // 메시지 설정
            Message message = Message.builder()
                    .putData("type", AlertTypeEnum.AUTO_LOGOUT.name())
                    .setAndroidConfig(config)
                    .setToken(member.getRegistrationToken())
                    .build();

            // 웹 API 토큰을 가져와서 보냄
            String response = FirebaseMessaging.getInstance().send(message);

            // 결과 출력
            log.info("Successfully sent message: " + response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlertErrorCode.ALERT_SERVER_ERROR);
        }

        return AutoLogoutValidDto.builder().success(true).build();
    }

    /**
     * 사용자에게 알람을 보낸다.
     *
     * @param groupId  그룹 id
     * @param nickname 알람을 보낼 사용자의 닉네임
     */
    @Override
    public void sendAlarm(Long groupId, String nickname) {

        Alarm alarm = alarmService.findAlarmByAlarmId(groupId);
        Member member = memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!memberAlarmRepository.existsByMemberAndAlarm(member, alarm)) {
            log.error("멤버 알람 정보가 존재하지 않음");
            throw new CustomException(MemberAlarmErrorCode.MEMBER_ALARM_NOT_FOUND);
        }
        sendAlarmTo(member.getRegistrationToken(), alarm.getId());
    }

    /**
     * 사용자에게 알람을 보낸다.
     *
     * @param targetToken 목표 기기 토큰
     * @param alarmId     그룹 아이디
     */
    private void sendAlarmTo(String targetToken, Long alarmId) {
        try {
            AndroidConfig config = AndroidConfig.builder()
                .setPriority(Priority.HIGH)
                .build();

            // 메시지 설정
            Message message = Message.builder()
                .putData("type", AlertTypeEnum.ALARM.name())
                .putData("alarmId", String.valueOf(alarmId))
                .setAndroidConfig(config)
                .setToken(targetToken)
                .build();

            // 웹 API 토큰을 가져와서 보냄
            String response = FirebaseMessaging.getInstance().send(message);
            // 결과 출력
            log.info("Successfully sent message: " + response);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(AlertErrorCode.ALERT_SERVER_ERROR);
        }
    }

    /**
     * 알림에 관한 명령이 본인것이 맞는지 확인한다.
     *
     * @param alert 알림
     */
    private void confirmAlertReceiver(Alert alert) {

        Member member = memberRepository.findByUsername(SecurityUtil.getCurrentUsername())
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (!alert.getReceiver().equals(member)) {
            throw new CustomException(AlertErrorCode.ALERT_MEMBER_NOT_EQUAL);
        }
    }
}
