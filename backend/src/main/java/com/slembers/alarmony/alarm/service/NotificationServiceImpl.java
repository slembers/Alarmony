package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    /**
     * 알림 객체를 받아서 초대 알림을 보낸다.
     *
     * @param notification
     */
    @Override
    public void sendInviteNotification(Notification notification) {

    }
}
