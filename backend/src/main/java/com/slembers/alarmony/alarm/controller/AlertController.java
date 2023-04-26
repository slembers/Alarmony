package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.response.AlertListResponseDto;
import com.slembers.alarmony.alarm.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/alert")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    /**
     * 내 알림 목록 가져오기
     *
     * @return 알림 목록
     */
    @GetMapping
    public ResponseEntity<AlertListResponseDto> getAlertList() {
        // TODO : 시큐리티에서 멤버 정보 얻어오기
        String username = "test";
        return new ResponseEntity<>(alertService.getAlertList(username), HttpStatus.OK);
    }

    /**
     * 알림 화면에서 특정 알림을 선택하여 개별로 지울 수 있다.
     *
     * @param alertId 알림 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{alert-id}")
    public ResponseEntity<String> deleteAlert(@PathVariable("alert-id") Long alertId) {
        alertService.deleteAlert(alertId);
        return new ResponseEntity<>("알림을 삭제했습니다.", HttpStatus.OK);
    }

    /**
     * 메시지 전송 테스트 메소드
     *
     * @return 성공 메시지
     */
    @PostMapping("/test")
    public ResponseEntity<String> testPushAlert() {
        alertService.testPushAlert();
        return new ResponseEntity<>("메시지 전송 성공", HttpStatus.OK);
    }
}
