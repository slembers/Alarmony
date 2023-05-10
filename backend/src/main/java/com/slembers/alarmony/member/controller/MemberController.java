package com.slembers.alarmony.member.controller;


import com.slembers.alarmony.global.dto.MessageResponseDto;
import com.slembers.alarmony.global.security.util.SecurityUtil;
import com.slembers.alarmony.member.dto.ChangePasswordDto;
import com.slembers.alarmony.member.dto.MemberInfoDto;
import com.slembers.alarmony.member.dto.request.*;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.dto.response.MemberResponseDto;
import com.slembers.alarmony.member.dto.response.TokenResponseDto;
import com.slembers.alarmony.member.service.EmailVerifyService;
import com.slembers.alarmony.member.service.MemberService;
import com.slembers.alarmony.report.dto.ModifiedMemberInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;


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
    public ResponseEntity<MessageResponseDto> signUp(@Valid @RequestBody SignUpDto signUpDto) {

        return new ResponseEntity<>(memberService.signUp(signUpDto), HttpStatus.CREATED);

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
     * 회원 가입 인증 이메일 확인
     */
    @GetMapping("/verify/{key}")
    public ResponseEntity<String> getVerify(@PathVariable String key) {

        emailVerifyService.verifyEmail(key);
        return new ResponseEntity<>("이메일 인증에 성공하였습니다.", HttpStatus.OK);

    }


    /**
     * test용 API
     */
    @GetMapping("/test")
    public void test() {
        log.info("[test 진입함]" + SecurityUtil.getCurrentUsername());
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
    public ResponseEntity<MessageResponseDto> findPassword(@RequestBody FindPasswordDto findPasswordDto) {

        return new ResponseEntity<>(memberService.findMemberPassword(findPasswordDto), HttpStatus.OK);

    }

    /**
     * 회원 정보 조회하기
     */
    @GetMapping("/info")
    public ResponseEntity<MemberResponseDto> getMemberInfo() {

        return new ResponseEntity<>(memberService.getMemberInfo(SecurityUtil.getCurrentUsername()), HttpStatus.OK);

    }


    /**
     * 회원 탈퇴 (비활성화)
     */
    @DeleteMapping()
    public ResponseEntity<MessageResponseDto> deleteMember() {

        return new ResponseEntity<>(memberService.deleteMember(SecurityUtil.getCurrentUsername()), HttpStatus.OK);

    }

    /**
     * 회원 정보 수정
     */
    @PatchMapping()
    public ResponseEntity<MemberInfoDto> modifyMemberInfo(@ModelAttribute ModifiedMemberInfoDto modifiedMemberInfoDto) throws IOException {

        log.info(modifiedMemberInfoDto.getNickname());
        return new ResponseEntity<>(memberService.modifyMemberInfo(SecurityUtil.getCurrentUsername(), modifiedMemberInfoDto), HttpStatus.OK);

    }


    /**
     * 비밀번호 변경
     */
    @PatchMapping("/change-pwd")
    public ResponseEntity<MessageResponseDto> changePassword(@RequestBody @Valid ChangePasswordDto changePasswordDto) {

        return new ResponseEntity<>(memberService.changePassword(SecurityUtil.getCurrentUsername(), changePasswordDto), HttpStatus.OK);

    }
}
