package com.slembers.alarmony.member.service;

import com.slembers.alarmony.member.dto.request.SignUpRequestDto;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.dto.vo.MemberVerificationDto;
import com.slembers.alarmony.member.entity.AuthorityEnum;
import com.slembers.alarmony.member.entity.Member;

public interface MemberService {

    /**
     * 회원 가입
     */

    MemberVerificationDto signUp(SignUpRequestDto signUpRequestDto);


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
     * 회원 권한 변경
     */
    void modifyUserRole(Member member, AuthorityEnum userRole);

    /**
     * username를 받으면 pk를 리턴
     */

    Member getMemberByUsername(String username);

    /**
     * nickname을 받으면 pk를 리턴
     */

    Member getMemberByNickname(String nickname);


}
