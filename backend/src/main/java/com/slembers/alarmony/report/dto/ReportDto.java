package com.slembers.alarmony.report.dto;

import com.slembers.alarmony.report.entity.ReportTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDto {

    private Long reportId;
    private ReportTypeEnum reportType;
    private String reporterUsername;
    private String reportedNickname;
    private String content;

}
