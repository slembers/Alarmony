package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.request.InviteMemberToGroupRequestDto;
import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.Notification;
import com.slembers.alarmony.alarm.entity.NotificationTypeEnum;
import com.slembers.alarmony.alarm.service.AlarmService;
import com.slembers.alarmony.alarm.service.NotificationService;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {


    private final MemberService memberService;

    private final  AlarmService alarmService;


    private final NotificationService notificationService;

    @PostMapping("/{group-id}/members")
    public ResponseEntity<String> inviteMemberToGroup(
        @PathVariable(name = "group-id") int groupId,
        InviteMemberToGroupRequestDto inviteMemberToGroupRequestDto) {

        // TODO: 시큐리티에서 멤버 정보 얻어오기
        Member sender = null;
        Alarm alarm = alarmService.getAlarmByAlarmId((long) groupId);

        // TODO: receiver 검증 로직 추가 -> 에러가 발생할 것 같으면 미리 처리하기
        inviteMemberToGroupRequestDto.getMembers().forEach(nickname ->
            notificationService.sendInviteNotification(
                Notification.builder()
                    .type(NotificationTypeEnum.INVITE)
                    .content(new StringBuilder().append("'").append(alarm.getTitle()).append("' 그룹 초대입니다.").toString())
                    .sender(sender)
                    .receiver(memberService.getMemberByNickName(nickname))
                    .alarm(alarm)
                    .build()
            )
        );
        return new ResponseEntity<>("멤버에게 그룹 초대를 요청했습니다.", HttpStatus.OK);
    }

}
