package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.response.AlarmListResponseDto;
import com.slembers.alarmony.alarm.entity.Alarm;

public interface AlarmService {

    /**
     * 유저네임을 기준으로 멤버알람 리스트를 가져오고, 이를 responseDTO에 담는다.
     *
     * @param username
     * @return
     */
    AlarmListResponseDto getAlarmList(String username);

    /**
     * 알람 ID로 알람 객체 가져오기
     *
     * @param id
     * @return
     */
    Alarm getAlarmByAlarmId(Long id);

    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     * @param alarmId
     * @param message
     */
    void putAlarmMessage(String username, Long alarmId, String message);
}
