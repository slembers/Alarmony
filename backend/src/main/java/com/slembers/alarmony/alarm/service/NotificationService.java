package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
import com.slembers.alarmony.alarm.entity.Notification;

public interface NotificationService {

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
    void sendInviteNotification(Notification notification);
}
