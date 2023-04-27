package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.CreateAlarmDto;
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
     * @return 알람 정보 리스트
     */
    @GetMapping
    public ResponseEntity<AlarmListResponseDto> getAlarmList() {
        // TODO : 시큐리티에서 멤버 정보 얻어오기
        String username = "test";
        return new ResponseEntity<>(alarmService.getAlarmList(username), HttpStatus.OK);
    }

    /**
     * 신규 알람을 생성한다.
     * @param createAlarmDto 생성 알람 정보
     * @return 성공 메시지
     */
    @PostMapping
    public ResponseEntity<String> createAlarm(@RequestBody CreateAlarmDto createAlarmDto) {
        // TODO : 시큐리티에서 멤버 정보 얻어오기
        String username = "test";
        alarmService.createAlarm(username, createAlarmDto);
        return new ResponseEntity<>("알람이 생성되고 초대가 전송되었습니다.", HttpStatus.OK);
    }

    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     *
     * @param alarmId                   알람 아이디
     * @param putAlarmMessageRequestDto 넣을 메시지
     * @return 확인 메시지
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
