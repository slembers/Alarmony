package com.slembers.alarmony.alarm.repository;

import com.slembers.alarmony.alarm.entity.Alert;
import com.slembers.alarmony.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findAllByReceiver(Member receiver);
}
