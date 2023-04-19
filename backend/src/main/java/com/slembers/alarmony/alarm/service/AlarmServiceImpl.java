package com.slembers.alarmony.alarm.service;

import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.alarm.repository.AlarmRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmRepository alarmRepository;

    /**
     * 알람 id를 사용하여 데이터베이스에서 알람 객체를 얻어온다.
     * 알람 정보가 존재하지 않을 경우 예외를 던진다.
     * @param id
     * @return 알람 객체
     */
    @Override
    public Alarm getAlarmByAlarmId(Long id) {
        return alarmRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }
}
