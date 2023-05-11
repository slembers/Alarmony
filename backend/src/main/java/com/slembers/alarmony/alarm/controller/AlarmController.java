package com.slembers.alarmony.alarm.controller;

import com.slembers.alarmony.alarm.dto.AlarmEndRecordDto;
import com.slembers.alarmony.alarm.dto.CreateAlarmDto;
import com.slembers.alarmony.alarm.dto.request.PutAlarmMessageRequestDto;
import com.slembers.alarmony.alarm.dto.AlarmDto;
import com.slembers.alarmony.alarm.dto.request.PutAlarmRecordTimeRequestDto;
import com.slembers.alarmony.alarm.dto.response.AlarmListResponseDto;
import com.slembers.alarmony.alarm.service.AlarmRecordService;
import com.slembers.alarmony.alarm.service.AlarmService;
import com.slembers.alarmony.global.security.util.SecurityUtil;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alarms")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    private final AlarmRecordService alarmRecordService;

    /**
     * 현재 로그인 유저의 알람 리스트를 가져온다
     *
     * @return 알람 정보 리스트
     */
    @GetMapping
    public ResponseEntity<AlarmListResponseDto> getAlarmList() {
        String username = SecurityUtil.getCurrentUsername();
        return new ResponseEntity<>(alarmService.getAlarmList(username), HttpStatus.OK);
    }

    /**
     * 신규 알람을 생성한다.
     *
     * @param createAlarmDto 생성 알람 정보
     * @return 성공 메시지
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAlarm(
        @Valid @RequestBody CreateAlarmDto createAlarmDto) {
        String username = SecurityUtil.getCurrentUsername();
        Long alarmId = alarmService.createAlarm(username, createAlarmDto);

        Map<String, Object> map = new HashMap<>();
        map.put("groupId", alarmId);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     *
     * @param alarmId                   알람 아이디
     * @param putAlarmMessageRequestDto 넣을 메시지
     * @return 확인 메시지
     */
    @PutMapping("/{alarm-id}/message")
    public ResponseEntity<Void> putAlarmMessage(
        @PathVariable("alarm-id") Long alarmId,
        @Valid @RequestBody PutAlarmMessageRequestDto putAlarmMessageRequestDto) {

        String username = SecurityUtil.getCurrentUsername();

        alarmService.putAlarmMessage(username, alarmId, putAlarmMessageRequestDto.getMessage());
        return ResponseEntity.noContent().build();
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

    /**
     * 알람 종료에 성공하면 기록을 저장한다.
     *
     * @param alarmId                      알람 아이디
     * @param putAlarmRecordTimeRequestDto 알람 성공 요청 객체
     * @return 성공 메시지
     */
    @PutMapping("/{alarm-id}/record")
    public ResponseEntity<Void> putAlarmRecord(@PathVariable("alarm-id") Long alarmId,
        @Valid @RequestBody PutAlarmRecordTimeRequestDto putAlarmRecordTimeRequestDto) {
        String username = SecurityUtil.getCurrentUsername();
        alarmRecordService.putAlarmRecord(AlarmEndRecordDto.builder()
            .alarmId(alarmId)
            .username(username)
            .datetime(putAlarmRecordTimeRequestDto.getDatetime())
            .success(true)
            .build());
        return ResponseEntity.noContent().build();
    }

    /**
     * 알람 종료에 실패시 기록을 저장한다.
     *
     * @param alarmId 알람 아이디
     * @return 실패 메시지
     */
    @PutMapping("/{alarm-id}/failed")
    public ResponseEntity<Void> putAlarmFailed(@PathVariable("alarm-id") Long alarmId) {
        String username = SecurityUtil.getCurrentUsername();
        alarmRecordService.putAlarmRecord(AlarmEndRecordDto.builder()
            .alarmId(alarmId)
            .username(username)
            .success(false)
            .build());
        return ResponseEntity.noContent().build();
    }
}
