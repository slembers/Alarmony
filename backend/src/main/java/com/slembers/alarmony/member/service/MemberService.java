package com.slembers.alarmony.member.service;

import com.slembers.alarmony.member.dto.request.SignUpDto;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;

public interface MemberService {

    /**
     * 회원 가입
     */

    boolean signUp(SignUpDto signUpDto);


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


}
