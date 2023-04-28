package com.slembers.alarmony.report.repository;

import com.slembers.alarmony.report.entity.ReportType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportTypeRepository extends JpaRepository<ReportType, Long> {

    /**
     * 신고 타입 이름으로 신고 타입을 찾는다.
     *
     * @param reportTypeName 신고 타입 이름
     * @return 신고 타입
     */
    Optional<ReportType> findByReportTypeName(String reportTypeName);

}
