package com.slembers.alarmony.member.service;

import com.slembers.alarmony.global.dto.MessageResponseDto;
import com.slembers.alarmony.member.dto.request.FindMemberIdDto;
import com.slembers.alarmony.member.dto.request.FindPasswordDto;
import com.slembers.alarmony.member.dto.request.ReissueTokenDto;
import com.slembers.alarmony.member.dto.request.SignUpDto;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.dto.response.MemberResponseDto;
import com.slembers.alarmony.member.dto.response.TokenResponseDto;

import javax.mail.MessagingException;


public interface MemberService {

    /**
     * 회원 가입
     */
    void signUp(SignUpDto signUpDto);


    /**
     * 아이디 중복체크
     **/
    CheckDuplicateDto checkForDuplicateId(String username);


    /**
     * 이메일 중복체크
     */
    CheckDuplicateDto checkForDuplicateEmail(String email);

    /**
     * 닉네임 중복 체크
     */
    CheckDuplicateDto checkForDuplicateNickname(String nickname);


    /**
     * 토큰 재발급
     */

    TokenResponseDto reissueToken(ReissueTokenDto reissueTokenDto);

    void putRegistrationToken(String username, String registrationToken);

    /**
     * 아이디 찾기
     */

    MessageResponseDto findMemberId (FindMemberIdDto findMemberIdDto) throws MessagingException;

    /**
     * 비밀 번호 찾기
     */

    void findMemberPassword(FindPasswordDto findPasswordDto);

    /**
     * 회원 정보 조회하기
     */

    MemberResponseDto getMemberInfo(String username);

    /**
     *  회원 탈퇴
     */
    MessageResponseDto deleteMember(String username);
}
