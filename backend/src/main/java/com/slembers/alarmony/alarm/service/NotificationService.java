package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.entity.Notification;

public interface NotificationService {

    /**
     * 알림 객체를 받아서 초대 알림을 보낸다.
     *
     * @param notification
     */
    void sendInviteNotification(Notification notification);
}
