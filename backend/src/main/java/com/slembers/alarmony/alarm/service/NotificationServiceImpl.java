package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.Notification;
import com.slembers.alarmony.alarm.entity.NotificationTypeEnum;
import com.slembers.alarmony.alarm.exception.AlarmErrorCode;
import com.slembers.alarmony.alarm.repository.AlarmRepository;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;

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
        Member sender = null;
        Alarm alarm = alarmRepository.findById(inviteMemberSetToGroupDto.getGroupId())
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        for (Member receiver : validMemberList) {
            sendInviteNotification(
                Notification.builder()
                    .type(NotificationTypeEnum.INVITE)
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
     * @param notification 알림 객체
     */
    @Override
    public void sendInviteNotification(Notification notification) {
        //TODO : 초대 보내는 것 FCM으로 전송하기.
    }
}
