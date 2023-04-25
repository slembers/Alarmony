package com.slembers.alarmony.alarm.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
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
     * @param notification 알림 객체
     */
    void sendInviteNotification(Alert notification);

    /**
     * 웹의 토큰을 가져오는 메소드 (테스트 이후 삭제)
     * @return 토큰
     * @throws IOException 예외
     */
    public String getAccessToken() throws IOException;


    /**
     * 메시지 전송하는 메소드 (추후 추가 커스텀 필요)
     * @param targetToken 목표 기기 토큰
     * @param title 제목
     * @param body 내용
     * @throws IOException 예외
     * @throws FirebaseMessagingException 파이어베이스 메시징 에러
     */
    public void sendMessageTo(String targetToken, String title, String body) throws IOException, FirebaseMessagingException;


    /**
     * 알림 테스트 메소드
     */
    void testPushAlert();

    AlertListResponseDto getAlertList(String username);
}
