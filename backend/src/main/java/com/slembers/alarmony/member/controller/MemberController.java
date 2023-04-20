package com.slembers.alarmony.member.controller;


import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


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
     * 인증 이메일 발송
     */
    @ApiOperation(value = "회원 가입시 이메인 인증", notes = "입력한 이메일로 인증번호 발송한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "인증번호 발송 성공"),
            @ApiResponse(code = 401, message = "인증번호 발송 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    @PostMapping("/email-confirm")
    public ResponseEntity<String> sendEmailVerificationCode(@RequestBody String email) {

        //존재하는 이메일인지 체크
        if(!memberService.checkForDuplicateEmail(email).isDuplicated()){

            //난수 생성


            //난수 생성으로 이메일 발급

            //redis에 인증번호 저장


            //

        }


        return new ResponseEntity<>("인증 번호 발송 완료", HttpStatus.OK);

    }
}
