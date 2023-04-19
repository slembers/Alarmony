package com.slembers.alarmony.alarm.repository;

import com.slembers.alarmony.alarm.entity.AlarmRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRecordRepository extends JpaRepository<AlarmRecord, Long> {
}
