package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
import com.slembers.alarmony.alarm.dto.request.InviteMemberToGroupRequestDto;
import com.slembers.alarmony.alarm.service.NotificationService;
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

    private final NotificationService notificationService;

    @PostMapping("/{group-id}/members")
    public ResponseEntity<String> inviteMemberToGroup(
        @PathVariable(name = "group-id") Long groupId,
        InviteMemberToGroupRequestDto inviteMemberToGroupRequestDto) {

        InviteMemberSetToGroupDto dto = InviteMemberSetToGroupDto.builder()
            .groupId(groupId)
            .nicknames(inviteMemberToGroupRequestDto.getMembers())
            .build();
        notificationService.inviteMemberToGroup(dto);
        return new ResponseEntity<>("멤버에게 그룹 초대를 요청했습니다.", HttpStatus.OK);
    }

}
