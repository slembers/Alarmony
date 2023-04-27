package com.slembers.alarmony.report.dto;

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
    private String reportType;
    private String reporter;
    private String reported;

}
