package com.slembers.alarmony.report.entity;

import javax.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "report_type")
public class ReportType {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_type_id")
    private Long id;

    @Column(name = "report_type_name", nullable = false)
    private String reportTypeName;

}
