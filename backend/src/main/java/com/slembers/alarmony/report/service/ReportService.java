package com.slembers.alarmony.report.service;

import com.slembers.alarmony.report.dto.ReportDto;
import com.slembers.alarmony.report.dto.response.ReportResponseDto;
import java.util.List;

public interface ReportService {

    /**
     * 신고 목록을 가져온다.
     *
     * @return 신고 목록
     */
    List<ReportDto> getReportList();

    /**
     * 신고 상세 정보를 가져온다.
     *
     * @param reportId 신고 id
     * @return 상제 정보
     */
    ReportResponseDto getReportDetail(Long reportId);

}
