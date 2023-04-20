package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.InviteMemberToGroupDto;
import com.slembers.alarmony.alarm.entity.Notification;

public interface NotificationService {

    /**
     * 멤버 집합을 돌며 초대 알림을 보낸다.
     * 
     * @param inviteMemberToGroupDto 그룹 초대에 필요한 dto
     */
    void inviteMemberToGroup(InviteMemberToGroupDto inviteMemberToGroupDto);

    /**
     * 알림 객체를 받아서 초대 알림을 보낸다.
     *
     * @param notification
     */
    void sendInviteNotification(Notification notification);
}
