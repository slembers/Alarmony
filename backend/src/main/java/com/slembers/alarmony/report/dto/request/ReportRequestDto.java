package com.slembers.alarmony.report.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportRequestDto {

    private String reportType;
    private String reportedNickname;
    private String content;

}
