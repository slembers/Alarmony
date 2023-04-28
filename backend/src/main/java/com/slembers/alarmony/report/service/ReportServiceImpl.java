package com.slembers.alarmony.report.service;

import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import com.slembers.alarmony.report.dto.ReportDto;
import com.slembers.alarmony.report.dto.response.ReportResponseDto;
import com.slembers.alarmony.report.entity.Report;
import com.slembers.alarmony.report.entity.ReportType;
import com.slembers.alarmony.report.exception.ReportErrorCode;
import com.slembers.alarmony.report.repository.ReportRepository;
import com.slembers.alarmony.report.repository.ReportTypeRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final MemberRepository memberRepository;
    private final ReportRepository reportRepository;
    private final ReportTypeRepository reportTypeRepository;

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
                .reportType(report.getReportType().getReportTypeName())
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
            .reportType(report.getReportType().getReportTypeName())
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
        ReportType reportType = reportTypeRepository.findByReportTypeName(reportDto.getReportType())
            .orElseThrow(() -> new CustomException(ReportErrorCode.REPORT_TYPE_NOT_FOUND));
        Member reporter = memberRepository.findByUsername(reportDto.getReporterUsername())
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        Member reported = memberRepository.findByNickname(reportDto.getReportedNickname())
            .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        Report report = Report.builder()
            .reportType(reportType)
            .reporter(reporter)
            .reported(reported)
            .content(reportDto.getContent())
            .build();
        reportRepository.save(report);
    }

}
