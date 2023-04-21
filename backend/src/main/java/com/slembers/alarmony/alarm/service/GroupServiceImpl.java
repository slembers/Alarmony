package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.member.dto.MemberInfoDto;
import com.slembers.alarmony.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final MemberRepository memberRepository;

    /**
     * 초대 가능한 멤버 리스트를 반환합니다.
     *
     * @param groupId 그룹 id
     * @param keyword 검색할 키워드
     * @return 초대 가능한 멤버 목록
     */
    @Override
    public List<MemberInfoDto> getInviteableMemberInfoList(Long groupId, String keyword) {
        return memberRepository.findMembersWithGroupAndTeamByGroupId(groupId, keyword);
    }

}
