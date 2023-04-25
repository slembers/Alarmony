package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.dto.response.AlarmRecordResponseDto;

public interface AlarmRecordService {

    /**
     * 오늘의 알람 기록을 얻어온다.
     *
     * @param groupId 그룹 id
     * @return 오늘 알람 기록
     */
    AlarmRecordResponseDto getTodayAlarmRecords(Long groupId);

}
