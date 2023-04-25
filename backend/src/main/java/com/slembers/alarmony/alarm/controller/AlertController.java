package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/notify")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    /**
     * 메시지 전송 테스트 메소드
     * @return
     */
    @PostMapping("/test")
    public ResponseEntity<String> testPushAlert(){
        alertService.testPushAlert();
        return new ResponseEntity<>("메시지 전송 성공", HttpStatus.OK);
    }
}
