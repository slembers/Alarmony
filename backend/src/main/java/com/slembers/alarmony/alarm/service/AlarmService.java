package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.AlarmDto;
import com.slembers.alarmony.alarm.dto.CreateAlarmDto;
import com.slembers.alarmony.alarm.dto.response.AlarmListResponseDto;
import com.slembers.alarmony.alarm.entity.Alarm;

public interface AlarmService {

    /**
     * 유저네임을 기준으로 멤버알람 리스트를 가져오고, 이를 responseDTO에 담는다.
     *
     * @param username 아이디
     * @return 알람리스트
     */
    AlarmListResponseDto getAlarmList(String username);

    /**
     * 신규 알람을 생성한다.
     *
     * @param username       현재 로그인 아이디
     * @param createAlarmDto 알람 생성 정보
     */
    Long createAlarm(String username, CreateAlarmDto createAlarmDto);

    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     *
     * @param alarmId 알람 아이디
     * @param message 메시지
     */
    void putAlarmMessage(String username, Long alarmId, String message);

    /**
     * 유저네임을 기준으로 특정 알람 정보를 가져온다.
     *
     * @param alarmId 알람 아이디
     * @return 알람 정보
     */
    AlarmDto getAlarmInfo(Long alarmId);


    /**
     * 알람 아이디로 알람 객체를 찾아온다.
     * @param alarmID 알람 아이디
     * @return 알람 객체
     */
    Alarm findAlarmByAlarmId(Long alarmID);
}
