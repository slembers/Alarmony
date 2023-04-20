package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.InviteMemberToGroupDto;
import com.slembers.alarmony.alarm.dto.request.InviteMemberToGroupRequestDto;
import com.slembers.alarmony.alarm.service.AlarmService;
import com.slembers.alarmony.alarm.service.NotificationService;
import com.slembers.alarmony.member.entity.Member;
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

    private final AlarmService alarmService;

    private final NotificationService notificationService;

    @PostMapping("/{group-id}/members")
    public ResponseEntity<String> inviteMemberToGroup(
        @PathVariable(name = "group-id") Long groupId,
        InviteMemberToGroupRequestDto inviteMemberToGroupRequestDto) {

        // TODO: 시큐리티에서 멤버 정보 얻어오기
        Member sender = null;
        notificationService.inviteMemberToGroup(InviteMemberToGroupDto.builder()
            .sender(sender)
            .alarm(alarmService.getAlarmByAlarmId(groupId))
            .nicknames(inviteMemberToGroupRequestDto.getMembers())
            .build()
        );
        return new ResponseEntity<>("멤버에게 그룹 초대를 요청했습니다.", HttpStatus.OK);
    }

}
