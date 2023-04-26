package com.slembers.alarmony.alarm.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.slembers.alarmony.alarm.dto.AlertDto;
import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
import com.slembers.alarmony.alarm.dto.response.AlertListResponseDto;
import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.Alert;
import com.slembers.alarmony.alarm.entity.AlertTypeEnum;
import com.slembers.alarmony.alarm.exception.AlarmErrorCode;
import com.slembers.alarmony.alarm.exception.AlertErrorCode;
import com.slembers.alarmony.alarm.repository.AlarmRepository;
import com.slembers.alarmony.alarm.repository.AlertRepository;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.util.UrlInfo;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;
    private final AlertRepository alertRepository;
    private final UrlInfo urlInfo;

    /**
     * 멤버 집합을 돌며 유효한 멤버에게 초대 알림을 보낸다.
     *
     * @param inviteMemberSetToGroupDto 그룹 초대에 필요한 dto
     */
    @Override
    public void inviteMemberToGroup(InviteMemberSetToGroupDto inviteMemberSetToGroupDto) {

        List<Member> validMemberList = new ArrayList<>();
        for (String nickname : inviteMemberSetToGroupDto.getNicknames()) {
            memberRepository.findByNickname(nickname)
                .ifPresent(validMemberList::add);
        }

        // TODO: 시큐리티에서 멤버 정보 얻어오기
        String username = "test";

        Member sender = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        Alarm alarm = alarmRepository.findById(inviteMemberSetToGroupDto.getGroupId())
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        for (Member receiver : validMemberList) {
            sendInviteAlert(
                Alert.builder()
                    .type(AlertTypeEnum.INVITE)
                    .content(String.format("'%s' 그룹 초대입니다.'",
                        alarm.getTitle()))
                    .sender(sender)
                    .receiver(receiver)
                    .alarm(alarm)
                    .build()
            );
        }
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
    public void testPushAlert() {
        try {
            sendMessageTo(this.getAccessToken(), "test", "This is Test Message");
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
            List<Alert> alerts = alertRepository.findAllByReceiver(member);
            List<AlertDto> alertDtos = new ArrayList<>();

            alerts.forEach(alert ->
                alertDtos.add(AlertDto.builder()
                    .id(alert.getId())
                    .profileImg(alert.getSender().getProfileImgUrl())
                    .content(alert.getContent())
                    .type(alert.getType().name())
                    .build()
                )
            );
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
     * 웹 토큰을 가져오는 메소드 (추후 수정)
     *
     * @return 토큰
     * @throws IOException 에러
     */
    public String getAccessToken() throws IOException {
        // firebase로 부터 access token을 가져온다.

        GoogleCredentials googleCredentials = GoogleCredentials
            .fromStream(new ClassPathResource("fcm-alert-config.json").getInputStream())
            .createScoped(Collections.singletonList(urlInfo.getCloudPlatformUrl()));

        googleCredentials.refreshIfExpired();

        return googleCredentials.getAccessToken().getTokenValue();

    }

    /**
     * @param targetToken 목표 기기 토큰
     * @param title       제목
     * @param body        내용
     */
    public void sendMessageTo(String targetToken, String title, String body) {
        try {
            // TODO : 현재는 토큰이 있는 기기가 적으므로, 추후에 토큰 설정을 포함하도록 변경해야 함.
            String targetMobile = "duAc2AEfShGR2Hf_hUTBRP:APA91bHmPeeBzhoz5nHiOzSEa9eIbWVv8l2uBAehRA3XVmh2L2qUguuNHVjpBirOhlHlT_qJsssZj6gHwLNScC1vS21tKP6hqcP-qqZmoGFA1eL61XdYuj301BUjCUHQ1MMNN3En1ub0";
            // 메시지 설정
            Message message = Message.builder()
                .setNotification(Notification.builder()
                    .setTitle("Alarmony")
                    .setBody("일해라 박성완")
                    .build())
                .setToken(targetMobile)
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
        // TODO: 닉네임으로 fcm 토큰 얻어오기
        sendAlarmTo("", alarm.getTitle());
    }

    /**
     * 사용자에게 알람을 보낸다.
     *
     * @param targetToken 목표 기기 토큰
     * @param groupTitle  그룹 타이틀
     */
    private void sendAlarmTo(String targetToken, String groupTitle) {
        try {
            // TODO : 현재는 토큰이 있는 기기가 적으므로, 추후에 토큰 설정을 포함하도록 변경해야 함.
            String targetMobile = "duAc2AEfShGR2Hf_hUTBRP:APA91bHmPeeBzhoz5nHiOzSEa9eIbWVv8l2uBAehRA3XVmh2L2qUguuNHVjpBirOhlHlT_qJsssZj6gHwLNScC1vS21tKP6hqcP-qqZmoGFA1eL61XdYuj301BUjCUHQ1MMNN3En1ub0";
            // 메시지 설정
            Message message = Message.builder()
                .putData("type", "ALARM")
                .putData("group", groupTitle)
                .setToken(targetMobile)
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
