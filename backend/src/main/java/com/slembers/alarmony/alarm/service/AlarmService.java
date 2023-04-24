package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.AlarmDto;
import com.slembers.alarmony.alarm.dto.response.AlarmListResponseDto;

public interface AlarmService {

    /**
     * 유저네임을 기준으로 멤버알람 리스트를 가져오고, 이를 responseDTO에 담는다.
     *
     * @param username
     * @return
     */
    AlarmListResponseDto getAlarmList(String username);

    /**
     * 특정 알람아이디를 주면, 알람 기록을 찾아서 메시지를 기록해둔다.
     *
     * @param alarmId
     * @param message
     */
    void putAlarmMessage(String username, Long alarmId, String message);

    /**
     * 유저네임을 기준으로 특정 알람 정보를 가져온다.
     *
     * @param alarmId 알람 아이디
     * @return 알람 정보
     */
    AlarmDto getAlarmInfo(Long alarmId);
}
