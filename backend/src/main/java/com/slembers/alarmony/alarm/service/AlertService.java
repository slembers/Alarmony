package com.slembers.alarmony.alarm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
import com.slembers.alarmony.alarm.dto.response.AlarmInviteResponseDto;
import com.slembers.alarmony.alarm.dto.response.AlertListResponseDto;
import com.slembers.alarmony.alarm.entity.Alert;

import java.io.IOException;

public interface AlertService {

    /**
     * 멤버 집합을 돌며 초대 알림을 보낸다.
     *
     * @param inviteMemberSetToGroupDto 그룹 초대에 필요한 dto
     */
    void inviteMemberToGroup(InviteMemberSetToGroupDto inviteMemberSetToGroupDto);

    /**
     * 알림 객체를 받아서 초대 알림을 보낸다.
     *
     * @param alert 알림 객체
     */
    void sendInviteAlert(Alert alert);


    /**
     * 메시지 전송하는 메소드 (추후 추가 커스텀 필요)
     *
     * @param targetToken 목표 기기 토큰
     * @param title       제목
     * @param body        내용
     * @throws IOException                예외
     * @throws FirebaseMessagingException 파이어베이스 메시징 에러
     */
    void sendMessageTo(String targetToken, String title, String body)
        throws IOException, FirebaseMessagingException;


    /**
     * 알림 테스트 메소드
     */
    void testPushAlert(String username);

    /**
     * 특정 유저의 알림 목록 가져오기
     *
     * @param username 아이디
     * @return 알림 목록
     */
    AlertListResponseDto getAlertList(String username);

    /**
     * 특정 알림을 선택하여 지울 수 있다.
     *
     * @param alertId 알림 아이디
     */
    void deleteAlert(Long alertId);

    /**
     * 사용자에게 알람을 보낸다.
     *
     * @param groupId  그룹 id
     * @param nickname 알람을 보낼 사용자의 닉네임
     */
    void sendAlarm(Long groupId, String nickname);


    /**
     * 초대 요청을 수락한다.
     * @param alertId 알림 아이디
     */
    AlarmInviteResponseDto acceptInvite(Long alertId);

    /**
     * 초대 요청을 거절한다.
     * @param alertId 알림 아이디
     */
    AlarmInviteResponseDto refuseInvite(Long alertId);

    /**
     * 알림을 커스텀해서 보낸다.
     * @param alert 알림
     * @param title 제목
     */
    void sendCustomAlert(Alert alert, String title);
}
