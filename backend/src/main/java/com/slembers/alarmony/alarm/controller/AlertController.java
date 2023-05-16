package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.request.CompareRegistTokenRequestDto;
import com.slembers.alarmony.alarm.dto.request.ResponseInviteRequestDto;
import com.slembers.alarmony.alarm.dto.response.AlarmInviteResponseDto;
import com.slembers.alarmony.alarm.dto.response.AlertListResponseDto;
import com.slembers.alarmony.alarm.dto.response.AutoLogoutValidDto;
import com.slembers.alarmony.alarm.exception.AlertErrorCode;
import com.slembers.alarmony.alarm.service.AlertService;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.security.util.SecurityUtil;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        String username = SecurityUtil.getCurrentUsername();
        return ResponseEntity.ok(alertService.getAlertList(username));
    }

    /**
     * 알림 화면에서 특정 알림을 선택하여 개별로 지울 수 있다.
     *
     * @param alertId 알림 아이디
     * @return 성공 메시지
     */
    @DeleteMapping("/{alert-id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable("alert-id") Long alertId) {
        alertService.deleteAlert(alertId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 그룹 초대에 대해 수락/거절 응답을 한다.
     *
     * @param alertId                  알림 아이디
     * @param responseInviteRequestDto 응답 객체 true : 수락, false : 거절
     * @return 응답 메시지
     */
    @PostMapping("/{alert-id}/response")
    public ResponseEntity<AlarmInviteResponseDto> responseInvite(
        @PathVariable("alert-id") Long alertId,
        @Valid @RequestBody ResponseInviteRequestDto responseInviteRequestDto) {

        if (responseInviteRequestDto == null || responseInviteRequestDto.getAccept() == null) {
            throw new CustomException(AlertErrorCode.ALERT_BAD_REQUEST);
        } else if (Boolean.TRUE.equals(responseInviteRequestDto.getAccept())) {
            return ResponseEntity.ok(alertService.acceptInvite(alertId));
        } else {
            return ResponseEntity.ok(alertService.refuseInvite(alertId));
        }
    }

    /**
     * 새로 로그인 할 때 원래 기기에 로그아웃 처리를 요청한다.
     * @param compareRegistTokenRequestDto 요청 객체
     * @return 성공 여부
     */
    @PostMapping("/auto-logout")
    public ResponseEntity<AutoLogoutValidDto> sendAutoLogoutAlert (@RequestBody CompareRegistTokenRequestDto compareRegistTokenRequestDto) {
        return ResponseEntity.ok(alertService.sendAutoLogoutAlert(SecurityUtil.getCurrentUsername(), compareRegistTokenRequestDto.getRegistToken()));
    }
}
