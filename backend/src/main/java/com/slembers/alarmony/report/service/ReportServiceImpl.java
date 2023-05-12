package com.slembers.alarmony.report.service;

import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.service.MemberService;
import com.slembers.alarmony.report.dto.ReportDto;
import com.slembers.alarmony.report.dto.response.ReportResponseDto;
import com.slembers.alarmony.report.entity.Report;
import com.slembers.alarmony.report.exception.ReportErrorCode;
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

    private final MemberService memberService;
    private final ReportRepository reportRepository;

    /**
     * 신고 목록을 가져온다.
     *
     * @return 신고 목록
     */
    @Override
    public List<ReportResponseDto> getReportList() {
        return reportRepository.findAll().stream()
            .map(report -> ReportResponseDto.builder()
                .reportId(report.getId())
                .reportType(String.valueOf(report.getReportType()))
                .reporterNickname(report.getReporter().getNickname())
                .reportedNickname(report.getReported().getNickname())
                .build())
            .collect(Collectors.toList());
    }

    /**
     * 신고 상세 정보를 가져온다.
     *
     * @param reportId 신고 id
     * @return 상제 정보
     */
    @Override
    public ReportResponseDto getReportDetail(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new CustomException(ReportErrorCode.REPORT_NOT_FOUND));
        return ReportResponseDto.builder()
            .reportId(report.getId())
            .reportType(String.valueOf(report.getReportType()))
            .reporterNickname(report.getReporter().getNickname())
            .reportedNickname(report.getReported().getNickname())
            .content(report.getContent())
            .build();
    }

    /**
     * 신고 정보를 등록한다.
     *
     * @param reportDto 신고 정보가 담긴 dto
     */
    @Override
    public void createReport(ReportDto reportDto) {
        Member reporter = memberService.findMemberByUsername(reportDto.getReporterUsername());
        Member reported = memberService.findMemberByNickName(reportDto.getReportedNickname());

        Report report = Report.builder()
            .reportType(reportDto.getReportType())
            .reporter(reporter)
            .reported(reported)
            .content(reportDto.getContent())
            .build();
        reportRepository.save(report);
    }

}
