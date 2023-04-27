package com.slembers.alarmony.report.entity;

import com.slembers.alarmony.member.entity.Member;

import javax.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "report_record")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_record_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_type", nullable = false)
    private ReportType reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id", nullable = false)
    private Member reported;

    @Column(name = "content", columnDefinition = "VARCHAR(1000)")
    private String content;

}
