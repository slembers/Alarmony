package com.slembers.alarmony.alarm.repository;

import com.slembers.alarmony.alarm.entity.Alert;
import com.slembers.alarmony.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    /**
     * 특정 멤버의 알림 리스트 가져오기
     * @param receiver 받은 사람
     * @return 알림 목록
     */
    List<Alert> findAllByReceiver(Member receiver);
}
