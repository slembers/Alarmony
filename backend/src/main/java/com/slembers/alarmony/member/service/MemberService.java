package com.slembers.alarmony.member.service;

import com.slembers.alarmony.member.dto.LoginDto;
import com.slembers.alarmony.member.dto.request.SignUpDto;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

public interface MemberService {

    /**
     * 회원 가입
     */
    void signUp(SignUpDto signUpDto);


    /**
     * 로그인
     */
    ResponseEntity<String> login(LoginDto loginDto, HttpServletResponse response);


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
