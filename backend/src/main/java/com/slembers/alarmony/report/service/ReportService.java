package com.slembers.alarmony.report.service;

import com.slembers.alarmony.report.dto.ReportDto;
import java.util.List;

public interface ReportService {

    /**
     * 신고 목록을 가져온다.
     *
     * @return 신고 목록
     */
    List<ReportDto> getReportList();

}