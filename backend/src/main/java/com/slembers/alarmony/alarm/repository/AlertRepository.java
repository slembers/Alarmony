package com.slembers.alarmony.alarm.repository;

import com.slembers.alarmony.alarm.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
}
