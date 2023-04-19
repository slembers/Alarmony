package com.slembers.alarmony.alarm.repository;

import com.slembers.alarmony.alarm.entity.MemberAlarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAlarmRepository extends JpaRepository<MemberAlarm, Long> {
}
