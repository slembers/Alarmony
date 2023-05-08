package com.slembers.alarmony.member.controller;


import com.slembers.alarmony.global.dto.MessageResponseDto;
import com.slembers.alarmony.global.security.util.SecurityUtil;
import com.slembers.alarmony.member.dto.request.*;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.dto.response.MemberResponseDto;
import com.slembers.alarmony.member.dto.response.TokenResponseDto;
import com.slembers.alarmony.member.service.EmailVerifyService;
import com.slembers.alarmony.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final EmailVerifyService emailVerifyService;

    /**
     * 회원가입
     */

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpDto signUpDto) {

        memberService.signUp(signUpDto);
        return new ResponseEntity<>(signUpDto.getNickname() + "님의 회원 가입을 완료했습니다. 이메일 인증을 확인해주세요", HttpStatus.CREATED);

    }

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
        log.info("test 진입함@@@@@@@@@@@@@@@@@@@@@" + SecurityUtil.getCurrentUsername()); //

    }

    /**
     * Access 토큰 및 Refresh 토큰 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestBody ReissueTokenDto reissueTokenDto) {

        return new ResponseEntity<>(memberService.reissueToken(reissueTokenDto), HttpStatus.OK);

    }

    @PutMapping("/regist-token")
    public ResponseEntity<String> putRegistrationToken(@RequestBody PutRegistrationTokenRequestDto registrationTokenRequestDto) {
        String username = SecurityUtil.getCurrentUsername();
        memberService.putRegistrationToken(username, registrationTokenRequestDto.getRegistrationToken());
        return new ResponseEntity<>("등록토큰 변경에 성공했습니다.", HttpStatus.OK);
    }


    /**
     * 아이디 찾기
     */
    @PostMapping("/find-id")
    public ResponseEntity<MessageResponseDto> findId(@RequestBody FindMemberIdDto findMemberIdDto) throws MessagingException {

        return new ResponseEntity<>(memberService.findMemberId(findMemberIdDto), HttpStatus.OK);
    }


    /**
     * 비밀번호 찾기
     */
    @PostMapping("/find-pw")
    public ResponseEntity<String> findPassword(@RequestBody FindPasswordDto findPasswordDto) {

        memberService.findMemberPassword(findPasswordDto);
        return new ResponseEntity<>("임시 비밀번호 발급", HttpStatus.OK);
    }

    /**
     * 회원 정보 조회하기
     */

    @GetMapping("/info")
    public ResponseEntity<MemberResponseDto> getMemberInfo() {

        String username = SecurityUtil.getCurrentUsername();
        log.info(username+ " /info 진입");
        return new ResponseEntity<>(memberService.getMemberInfo(username), HttpStatus.OK);
    }
}
