package com.slembers.alarmony.report.controller;

import com.slembers.alarmony.report.dto.ReportDto;
import com.slembers.alarmony.report.service.ReportService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * 신고 목록을 가져온다.
     *
     * @return 신고 목록
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getReportList() {
        List<ReportDto> reportList = reportService.getReportList();

        Map<String, Object> map = new HashMap<>();
        map.put("reports", reportList);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}