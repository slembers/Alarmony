package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.response.AlarmListResponseDto;

public interface AlarmService {

    /**
     * 유저네임을 기준으로 멤버알람 리스트를 가져오고, 이를 responseDTO에 담는다.
     *
     * @param username
     * @return
     */
    AlarmListResponseDto getAlarmList(String username);

}
