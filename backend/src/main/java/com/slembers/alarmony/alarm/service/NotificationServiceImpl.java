package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.InviteMemberToGroupDto;
import com.slembers.alarmony.alarm.entity.Notification;
import com.slembers.alarmony.alarm.entity.NotificationTypeEnum;
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

    /**
     * 멤버 집합을 돌며 유효한 멤버에게 초대 알림을 보낸다.
     *
     * @param inviteMemberToGroupDto 그룹 초대에 필요한 dto
     */
    @Override
    public void inviteMemberToGroup(InviteMemberToGroupDto inviteMemberToGroupDto) {

        List<Member> validMemberList = new ArrayList<>();
        for (String nickname : inviteMemberToGroupDto.getNicknames()) {
            memberRepository.findByNickname(nickname)
                .ifPresent(validMemberList::add);
        }

        for (Member receiver : validMemberList) {
            sendInviteNotification(
                Notification.builder()
                    .type(NotificationTypeEnum.INVITE)
                    .content(String.format("'%s' 그룹 초대입니다.'",
                        inviteMemberToGroupDto.getAlarm().getTitle()))
                    .sender(inviteMemberToGroupDto.getSender())
                    .receiver(receiver)
                    .alarm(inviteMemberToGroupDto.getAlarm())
                    .build()
            );
        }
    }

    /**
     * 알림 객체를 받아서 초대 알림을 보낸다.
     *
     * @param notification
     */
    @Override
    public void sendInviteNotification(Notification notification) {

    }
}
