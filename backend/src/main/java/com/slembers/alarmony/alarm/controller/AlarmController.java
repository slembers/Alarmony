package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.request.PutAlarmMessageRequestDto;
import com.slembers.alarmony.alarm.dto.AlarmDto;
import com.slembers.alarmony.alarm.dto.response.AlarmListResponseDto;
import com.slembers.alarmony.alarm.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     *
     * @param alarmId
     * @param putAlarmMessageRequestDto
     * @return
     */
    @PutMapping("/{alarm-id}/message")
    public ResponseEntity<String> putAlarmMessage(
            @PathVariable("alarm-id") Long alarmId,
            @RequestBody PutAlarmMessageRequestDto putAlarmMessageRequestDto) {

        // TODO : 시큐리티에서 멤버 정보 얻어오기
        String username = "test";

        alarmService.putAlarmMessage(username, alarmId, putAlarmMessageRequestDto.getMessage());
        return new ResponseEntity<>("알람 메시지가 기록되었습니다.", HttpStatus.OK);
    }

    /**
     * 특정 알람 아이디로 알람 정보를 가져온다
     *
     * @param alarmId 알람 아이디
     * @return 알람 정보
     */
    @GetMapping("/{alarm-id}")
    public ResponseEntity<AlarmDto> getAlarmInfo(@PathVariable("alarm-id") Long alarmId) {
        return new ResponseEntity<>(alarmService.getAlarmInfo(alarmId), HttpStatus.OK);
    }
}
