package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.member.dto.MemberInfoDto;
import java.util.List;

public interface GroupService {

    /**
     * 유저가 그룹의 주인인지 확인합니다.
     *
     * @param groupId  그룹 id
     * @param username 유저네임
     */
    boolean isGroupOwner(Long groupId, String username);

    /**
     * 그룹 주인의 닉네임이 일치하는지 확인합니다.
     *
     * @param groupId  그룹 id
     * @param nickname 닉네임
     */
    boolean isGroupOwnerByNickname(Long groupId, String nickname);

    /**
     * 초대 가능한 멤버 리스트를 반환합니다.
     *
     * @param groupId 그룹 id
     * @param keyword 검색할 키워드
     * @return 초대 가능한 멤버 목록
     */
    List<MemberInfoDto> getInviteableMemberInfoList(Long groupId, String keyword, String username);

    /**
     * 그룹에서 호스트 멤버를 제외한다.
     *
     * @param groupId 그룹 id
     */
    void removeHostMember(Long groupId);

    /**
     * 그룹에서 유저네임을 기준으로 멤버를 제외한다.
     *
     * @param groupId  그룹 id
     * @param username 그룹에서 제외할 멤버 유저네임
     */
    void removeMemberByUsername(Long groupId, String username);

    /**
     * 그룹에서 닉네임을 기준으로 멤버를 제외한다.
     *
     * @param groupId  그룹 id
     * @param nickname 그룹에서 제외할 멤버 닉네임
     */
    void removeMemberByNickname(Long groupId, String nickname);

}
