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
     * 유저가 그룹의 주인인지 확인합니다.
     *
     * @param groupId  그룹 id
     * @param username 유저네임
     */
    @Override
    public boolean isGroupOwner(Long groupId, String username) {
        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        Alarm alarm = alarmRepository.findById(groupId)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        return member.equals(alarm.getHost());
    }

    /**
     * 그룹 주인의 닉네임이 일치하는지 확인합니다.
     *
     * @param groupId  그룹 id
     * @param nickname 닉네임
     */
    @Override
    public boolean isGroupOwnerByNickname(Long groupId, String nickname) {
        Alarm alarm = alarmRepository.findById(groupId)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        return alarm.getHost().getNickname().equals(nickname);
    }

    /**
     * 초대 가능한 멤버 리스트를 반환합니다.
     *
     * @param groupId  그룹 id
     * @param keyword  검색할 키워드
     * @param username 제외할 멤버의 유저네임
     * @return 초대 가능한 멤버 목록
     */
    @Override
    public List<MemberInfoDto> getInviteableMemberInfoList(Long groupId, String keyword,
        String username) {

        Member member = memberRepository.findByUsername(username)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        return memberRepository.findMembersWithGroupAndTeamByGroupId(groupId, keyword,
            member.getId());
    }

    /**
     * 그룹에서 호스트 멤버를 제외한다.
     *
     * @param groupId 그룹 id
     */
    @Transactional
    @Override
    public void removeHostMember(Long groupId) {
        if (memberAlarmRepository.countByAlarmId(groupId) != 1) {
            log.error("그룹장은 그룹에 멤버가 존재하지 않아야 탈퇴할 수 있음");
            throw new CustomException(AlarmErrorCode.MEMBER_IN_GROUP);
        }

        Alarm alarm = alarmRepository.findById(groupId)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        MemberAlarm memberAlarm = memberAlarmRepository.findByMemberAndAlarm(alarm.getHost(), alarm)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.MEMBER_NOT_IN_GROUP));
        alarmRecordRepository.deleteByMemberAlarm(memberAlarm);
        memberAlarmRepository.delete(memberAlarm);

        alarmRepository.delete(alarm);
    }

    /**
     * 그룹에서 유저네임을 기준으로 멤버를 제외한다.
     *
     * @param groupId  그룹 id
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

    /**
     * 그룹에서 닉네임을 기준으로 멤버를 제외한다.
     *
     * @param groupId  그룹 id
     * @param nickname 그룹에서 제외할 멤버 닉네임
     */
    @Transactional
    @Override
    public void removeMemberByNickname(Long groupId, String nickname) {
        Member member = memberRepository.findByNickname(nickname)
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        Alarm alarm = alarmRepository.findById(groupId)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.ALARM_NOT_FOUND));

        MemberAlarm memberAlarm = memberAlarmRepository.findByMemberAndAlarm(member, alarm)
            .orElseThrow(() -> new CustomException(AlarmErrorCode.MEMBER_NOT_IN_GROUP));
        alarmRecordRepository.deleteByMemberAlarm(memberAlarm);
        memberAlarmRepository.delete(memberAlarm);
    }

}
