package com.slembers.alarmony.member.controller;


import com.slembers.alarmony.member.dto.LoginDto;
import com.slembers.alarmony.member.dto.request.SignUpDto;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.service.EmailVerifyService;
import com.slembers.alarmony.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailVerifyService emailVerifyService;



    /**
     * 회원가입
     */

    @PostMapping()
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpDto signUpDto) {

        memberService.signUp(signUpDto);
        return new ResponseEntity<>("회원 가입 성공", HttpStatus.CREATED);

    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public void login(@RequestBody LoginDto loginDto, HttpServletResponse response){

    }



    //가입된 유저인지 아이디와 비밀번호 검증 (Service Layer)
    //AccessToken 발급 (Controller Layer)


    /**
     * 로그아웃
     */

    /**
     * 아이디 중복 체크
     **/
    @GetMapping("/check-id")
    public ResponseEntity<CheckDuplicateDto> checkForDuplicateId(@RequestParam("username") String username) {

        return new ResponseEntity<>(memberService.checkForDuplicateId(username), HttpStatus.OK);
    }


    /**
     * 이메일 중복 체크
     */
    @GetMapping("/check-email")
    public ResponseEntity<CheckDuplicateDto> checkForDuplicateEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>(memberService.checkForDuplicateEmail(email), HttpStatus.OK);
    }

    /**
     * 닉네임 중복 체크
     */

    @GetMapping("/check-nickname")
    public ResponseEntity<CheckDuplicateDto> checkForDuplicateNickname(@RequestParam("nickname") String nickname) {
        return new ResponseEntity<>(memberService.checkForDuplicateNickname(nickname), HttpStatus.OK);
    }



    /**
     * 인증 이메일 확인
     */
    @GetMapping("/verify/{key}")
    public ResponseEntity<String> getVerify(@PathVariable String key) {

        emailVerifyService.verifyEmail(key);
        return new ResponseEntity<>("이메일 인증에 성공하였습니다.", HttpStatus.OK);

    }

}
