package com.slembers.alarmony.member.controller;


import com.slembers.alarmony.global.jwt.JwtTokenProvider;
import com.slembers.alarmony.global.jwt.SecurityUtil;
import com.slembers.alarmony.global.redis.service.RedisUtil;
import com.slembers.alarmony.member.dto.LoginDto;
import com.slembers.alarmony.member.dto.request.ReissueTokenDto;
import com.slembers.alarmony.member.dto.request.SignUpDto;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.dto.response.ReissueTokenResponseDto;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.service.EmailVerifyService;
import com.slembers.alarmony.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final EmailVerifyService emailVerifyService;

    private final RedisUtil redisUtil;

    private final JwtTokenProvider jwtTokenProvider;


    /**
     * 회원가입
     */

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpDto signUpDto) {

        log.info("[회원 가입 Controller 들어왔음");
        memberService.signUp(signUpDto);
        return new ResponseEntity<>("회원 가입 성공", HttpStatus.CREATED);

    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {

        return memberService.login(loginDto, response);

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


    @GetMapping("/test")
    public void test(@AuthenticationPrincipal User user) {


        user.getAuthorities();
        log.info("test진입");
        log.info("test진입" + user.getAuthorities());

        log.info("test 진입함@@@@@@@@@@@@@@@@@@@@@" + SecurityUtil.getCurrentUsername().get()); //

    }

    /**
     * Access 토큰 및 Refresh 토큰 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<ReissueTokenResponseDto> refresh(@RequestBody ReissueTokenDto reissueTokenDto){


       return new ResponseEntity<ReissueTokenResponseDto>(memberService.reissueToken(reissueTokenDto), HttpStatus.OK);




    }

}
