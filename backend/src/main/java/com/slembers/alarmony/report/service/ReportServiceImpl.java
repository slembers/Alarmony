package com.slembers.alarmony.report.service;

import com.slembers.alarmony.report.dto.ReportDto;
import com.slembers.alarmony.report.repository.ReportRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    /**
     * 신고 목록을 가져온다.
     *
     * @return 신고 목록
     */
    @Override
    public List<ReportDto> getReportList() {
        return reportRepository.findAll().stream()
            .map(report -> ReportDto.builder().reportId(report.getId())
                .reportType(report.getReportType().getReportTypeName())
                .reporter(report.getReporter().getNickname())
                .reported(report.getReported().getNickname())
                .build())
            .collect(Collectors.toList());
    }

}
