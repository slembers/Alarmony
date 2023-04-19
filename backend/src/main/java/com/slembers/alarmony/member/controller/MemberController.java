package com.slembers.alarmony.member.controller;


import com.slembers.alarmony.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/check-id")
    public ResponseEntity<Boolean> checkForDuplicateId(@RequestParam("username") String username) {


        return new ResponseEntity<>(memberService.checkForDuplicateId(username), HttpStatus.OK);
    }
}
