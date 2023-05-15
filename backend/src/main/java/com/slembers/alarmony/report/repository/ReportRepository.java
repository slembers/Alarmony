package com.slembers.alarmony.report.repository;

import com.slembers.alarmony.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    /**
     * 신고자 아이디와 일치하는 모든 리포트를 삭제한다.
     *
     * @param reporterId 신고자 id
     */
    @Modifying
    @Query("DELETE FROM report_record r WHERE r.reporter.id = :reporterId")
    void deleteByReporterId(Long reporterId);

    /**
     * 피신고자 아이디와 일치하는 모든 리포트를 삭제한다.
     *
     * @param reportedId 피신고자 id
     */
    @Modifying
    @Query("DELETE FROM report_record r WHERE r.reported.id = :reportedId")
    void deleteByReportedId(Long reportedId);

}
