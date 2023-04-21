package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
import com.slembers.alarmony.alarm.dto.request.InviteMemberToGroupRequestDto;
import com.slembers.alarmony.alarm.service.GroupService;
import com.slembers.alarmony.alarm.service.NotificationService;
import com.slembers.alarmony.member.dto.MemberInfoDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    private final NotificationService notificationService;

    /**
     * 초대 가능한 멤버 리스트를 반환합니다.
     *
     * @param groupId 그룹 id
     * @param keyword 검색할 키워드
     * @return 초대 가능한 멤버 목록
     */
    @GetMapping("/inviteable-members")
    public ResponseEntity<Map<String, Object>> getInviteableMembers(
        @RequestParam(value = "group-id", required = false) Long groupId,
        @RequestParam(value = "keyword", required = false) String keyword) {

        List<MemberInfoDto> memberInfoList = groupService.getInviteableMemberInfoList(
            groupId == null ? -1 : groupId, keyword);

        Map<String, Object> map = new HashMap<>();
        map.put("memberInfoList", memberInfoList);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 멤버를 그룹으로 초대합니다.
     *
     * @param groupId 그룹 id
     * @param inviteMemberToGroupRequestDto 그룹 초대에 필요한 dto (멤버 닉네임 집합)
     * @return 성공 여부
     */
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
