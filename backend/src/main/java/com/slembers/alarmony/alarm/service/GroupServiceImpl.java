package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.MemberAlarm;
import com.slembers.alarmony.alarm.exception.AlarmErrorCode;
import com.slembers.alarmony.alarm.repository.AlarmRecordRepository;
import com.slembers.alarmony.alarm.repository.AlarmRepository;
import com.slembers.alarmony.alarm.repository.MemberAlarmRepository;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.member.dto.MemberInfoDto;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final MemberRepository memberRepository;
    private final AlarmRepository alarmRepository;
    private final MemberAlarmRepository memberAlarmRepository;
    private final AlarmRecordRepository alarmRecordRepository;

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

    /**
     * 그룹에서 유저네임을 기준으로 멤버를 제외한다.
     *
     * @param groupId 그룹 id
     * @param username 그룹에서 제외할 멤버 유저네임
     */
    @Transactional
    @Override
    public void removeMemberByUsername(Long groupId, String username) {
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        Alarm alarm = alarmRepository.findById(groupId)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        MemberAlarm memberAlarm = memberAlarmRepository.findByMemberAndAlarm(member, alarm)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.MEMBER_NOT_IN_GROUP));

        alarmRecordRepository.deleteByMemberAlarm(memberAlarm);
        memberAlarmRepository.delete(memberAlarm);
    }
}
