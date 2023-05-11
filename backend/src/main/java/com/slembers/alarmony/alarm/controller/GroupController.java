package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.AlarmRecordDto;
import com.slembers.alarmony.alarm.dto.InviteMemberSetToGroupDto;
import com.slembers.alarmony.alarm.dto.MemberRankingDto;
import com.slembers.alarmony.alarm.dto.request.InviteMemberToGroupRequestDto;
import com.slembers.alarmony.alarm.exception.AlarmErrorCode;
import com.slembers.alarmony.alarm.service.AlarmRecordService;
import com.slembers.alarmony.alarm.service.GroupService;
import com.slembers.alarmony.alarm.service.AlertService;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.security.util.SecurityUtil;
import com.slembers.alarmony.member.dto.MemberInfoDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final AlarmRecordService alarmRecordService;
    private final AlertService alertService;

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
        @RequestParam(value = "keyword") String keyword) {

        String username = SecurityUtil.getCurrentUsername();
        List<MemberInfoDto> memberInfoList = groupService.getInviteableMemberInfoList(
            groupId == null ? -1 : groupId, keyword, username);

        Map<String, Object> map = new HashMap<>();
        map.put("memberInfoList", memberInfoList);
        return ResponseEntity.ok(map);
    }

    /**
     * 멤버를 그룹으로 초대합니다.
     *
     * @param groupId                       그룹 id
     * @param inviteMemberToGroupRequestDto 그룹 초대에 필요한 dto (멤버 닉네임 집합)
     * @return 성공 여부
     */
    @PostMapping("/{group-id}/members")
    public ResponseEntity<Map<String, Object>> inviteMemberToGroup(
        @PathVariable(name = "group-id") Long groupId,
        @Valid @RequestBody InviteMemberToGroupRequestDto inviteMemberToGroupRequestDto) {

        String username = SecurityUtil.getCurrentUsername();

        InviteMemberSetToGroupDto dto = InviteMemberSetToGroupDto.builder()
            .groupId(groupId)
            .nicknames(inviteMemberToGroupRequestDto.getMembers())
            .sender(username)
            .build();
        int cnt = alertService.inviteMemberToGroup(dto);

        Map<String, Object> map = new HashMap<>();
        map.put("success", cnt);
        map.put("total", inviteMemberToGroupRequestDto.getMembers().size());
        return ResponseEntity.ok(map);
    }

    /**
     * 그룹에서 떠납니다.
     *
     * @param groupId 그룹 id
     * @return 성공 여부
     */
    @DeleteMapping("/{group-id}")
    public ResponseEntity<Void> leaveFromGroup(
        @PathVariable(name = "group-id") Long groupId) {
        String username = SecurityUtil.getCurrentUsername();

        if (groupService.isGroupOwner(groupId, username)) {
            groupService.removeHostMember(groupId);
        } else {
            groupService.removeMemberByUsername(groupId, username);
        }
        return ResponseEntity.noContent().build();
    }

    /**
     * 그룹장 권한으로 그룹에서 멤버를 퇴출합니다.
     *
     * @param groupId  그룹 ID
     * @param nickname 퇴출할 멤버의 nickname
     * @return 성공 여부
     */
    @DeleteMapping("/{group-id}/members/{nickname}")
    public ResponseEntity<Void> removeMemberFromGroup(
        @PathVariable(name = "group-id") Long groupId,
        @PathVariable(name = "nickname") String nickname) {

        String username = SecurityUtil.getCurrentUsername();

        if (!groupService.isGroupOwner(groupId, username)) {
            throw new CustomException(AlarmErrorCode.MEMBER_NOT_HOST);
        }
        if (groupService.isGroupOwnerByNickname(groupId, nickname)) {
            throw new CustomException(AlarmErrorCode.CANNOT_REMOVE_HOST);
        }

        groupService.removeMemberByNickname(groupId, nickname);
        return ResponseEntity.noContent().build();
    }

    /**
     * 오늘의 알람 기록 정보를 가져온다.
     *
     * @param groupId 그룹 id
     * @return 알람 기록
     */
    @GetMapping("/{group-id}/records")
    public ResponseEntity<Map<String, Object>> getTodayAlarmRecords(
        @PathVariable(name = "group-id") Long groupId) {

        List<AlarmRecordDto> alarmList = alarmRecordService.getTodayAlarmRecords(groupId);

        Map<String, Object> map = new HashMap<>();
        map.put("alarmList", alarmList);
        return ResponseEntity.ok(map);
    }

    /**
     * 알람 랭킹 기록을 얻어온다.
     *
     * @param groupId 그룹 id
     * @return 알람 랭킹 기록
     */
    @GetMapping("/{group-id}/ranks")
    public ResponseEntity<Map<String, Object>> getAlarmRanking(
        @PathVariable(name = "group-id") Long groupId) {

        List<MemberRankingDto> alarmRanking = alarmRecordService.getAlarmRanking(groupId);

        Map<String, Object> map = new HashMap<>();
        map.put("members", alarmRanking);
        return ResponseEntity.ok(map);
    }

    /**
     * 사용자에게 알람 보내기
     *
     * @param groupId  그룹 id
     * @param nickname 알람 보낼 사람의 닉네임
     * @return 성공 여부
     */
    @PostMapping("/{group-id}/members/{nickname}/alarms")
    public ResponseEntity<Void> sendAlarm(
        @PathVariable(name = "group-id") Long groupId,
        @PathVariable(name = "nickname") String nickname) {

        String username = SecurityUtil.getCurrentUsername();

        if (!groupService.isGroupOwner(groupId, username)) {
            throw new CustomException(AlarmErrorCode.MEMBER_NOT_HOST);
        }
        if (groupService.isGroupOwnerByNickname(groupId, nickname)) {
            throw new CustomException(AlarmErrorCode.CANNOT_SEND_TO_HOST);
        }

        alertService.sendAlarm(groupId, nickname);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
