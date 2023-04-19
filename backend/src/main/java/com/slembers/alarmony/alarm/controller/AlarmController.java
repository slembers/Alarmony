package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.response.AlarmListResponseDto;
import com.slembers.alarmony.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    /**
     * 현재 로그인 유저의 알람 리스트를 가져온다
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<AlarmListResponseDto> getAlarmList() {
        // 추후에 jwt에서 찾은 유저네임으로 바꾸어야 한다
        String username = "test";
        return new ResponseEntity<>(alarmService.getAlarmList(username), HttpStatus.OK);
    }
}
