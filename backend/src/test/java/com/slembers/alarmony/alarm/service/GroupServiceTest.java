package com.slembers.alarmony.alarm.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.entity.MemberAlarm;
import com.slembers.alarmony.alarm.exception.AlarmErrorCode;
import com.slembers.alarmony.alarm.repository.AlarmRepository;
import com.slembers.alarmony.alarm.repository.MemberAlarmRepository;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @InjectMocks
    private GroupServiceImpl groupService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private AlarmRepository alarmRepository;

    @Mock
    private MemberAlarmRepository memberAlarmRepository;

    @Test
    @DisplayName("유저네임으로 조회: 그룹장이 맞음")
    void isGroupOwner() {
        // given
        Member member = Member.builder()
            .username("member1")
            .build();
        Alarm alarm = Alarm.builder()
            .host(member)
            .build();

        // mocking
        given(memberRepository.findByUsername("member1")).willReturn(Optional.of(member));
        given(alarmRepository.findById(1L)).willReturn(Optional.of(alarm));

        // when
        boolean res = groupService.isGroupOwner(1L, "member1");

        // then
        assertTrue(res);
    }

    @Test
    @DisplayName("유저네임으로 조회: 그룹장이 아님")
    void isGroupOwnerFail() {
        // given
        Member member1 = Member.builder()
            .username("member1")
            .build();
        Member member2 = Member.builder()
            .username("member2")
            .build();
        Alarm alarm = Alarm.builder()
            .host(member1)
            .build();

        // mocking
        given(memberRepository.findByUsername("member2")).willReturn(Optional.of(member2));
        given(alarmRepository.findById(1L)).willReturn(Optional.of(alarm));

        // when
        boolean res = groupService.isGroupOwner(1L, "member2");

        // then
        assertFalse(res);
    }

    @Test
    @DisplayName("유저네임으로 조회: 멤버가 존재하지 않음")
    void isGroupOwnerMemberNotFound() {
        // given
        // mocking
        given(memberRepository.findByUsername("member2")).willReturn(Optional.empty());

        // when
        // then
        CustomException exception = assertThrows(CustomException.class,
            () -> groupService.isGroupOwner(1L, "member2"));
        assertEquals(MemberErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("유저네임으로 조회: 알람이 존재하지 않음")
    void isGroupOwnerAlarmNotFound() {
        // given
        Member member1 = Member.builder()
            .username("member1")
            .build();

        // mocking
        given(memberRepository.findByUsername("member1")).willReturn(Optional.of(member1));
        given(alarmRepository.findById(1L)).willReturn(Optional.empty());

        // when
        // then
        CustomException exception = assertThrows(CustomException.class,
            () -> groupService.isGroupOwner(1L, "member1"));
        assertEquals(AlarmErrorCode.ALARM_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("닉네임으로 조회: 그룹장이 맞음")
    void isGroupOwnerByNickname() {
        // given
        Member member1 = Member.builder()
            .nickname("member1")
            .build();
        Alarm alarm = Alarm.builder()
            .host(member1)
            .build();

        // mocking
        given(alarmRepository.findById(1L)).willReturn(Optional.of(alarm));

        // when
        boolean res = groupService.isGroupOwnerByNickname(1L, member1.getNickname());

        // then
        assertTrue(res);
    }

    @Test
    @DisplayName("닉네임으로 조회: 그룹장이 아님")
    void isGroupOwnerByNicknameFail() {
        // given
        Member member1 = Member.builder()
            .nickname("member1")
            .build();
        Member member2 = Member.builder()
            .nickname("member2")
            .build();
        Alarm alarm = Alarm.builder()
            .host(member1)
            .build();

        // mocking
        given(alarmRepository.findById(1L)).willReturn(Optional.of(alarm));

        // when
        boolean res = groupService.isGroupOwnerByNickname(1L, member2.getNickname());

        // then
        assertFalse(res);
    }

    @Test
    @DisplayName("그룹에 멤버가 존재하면 호스트가 그룹을 나갈 수 없음")
    void removeHostMember() {
        // given

        // mocking
        given(memberAlarmRepository.countByAlarmId(1L)).willReturn(2);

        // when
        // then
        CustomException exception = assertThrows(CustomException.class,
            () ->  groupService.removeHostMember(1L));
        assertEquals(AlarmErrorCode.MEMBER_IN_GROUP, exception.getErrorCode());
    }

    @Test
    @DisplayName("해당 멤버가 그룹에 존재하지 않는다면 나갈 수 없음")
    void removeMemberByUsername() {
        // given
        Member member1 = Member.builder()
            .username("member1")
            .build();
        Alarm alarm = Alarm.builder()
            .build();
//        MemberAlarm memberAlarm = MemberAlarm.builder()
//            .member(member1)
//            .alarm(alarm)
//            .build();

        // mocking
        given(memberRepository.findByUsername("member1")).willReturn(Optional.of(member1));
        given(alarmRepository.findById(1L)).willReturn(Optional.of(alarm));
//        given(memberAlarmRepository.findByMemberAndAlarm(member1, alarm)).willReturn(Optional.of(memberAlarm));

        // when
        // then
        CustomException exception = assertThrows(CustomException.class,
            () -> groupService.removeMemberByUsername(1L, "member1"));
        assertEquals(AlarmErrorCode.MEMBER_NOT_IN_GROUP, exception.getErrorCode());
    }

}
