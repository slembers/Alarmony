package com.slembers.alarmony.report.entity;

import com.slembers.alarmony.member.entity.Member;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "report_record")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_record_id")
    private Long id;

    @Column(name = "report_type", nullable = false)
    private ReportTypeEnum reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter_id", nullable = false)
    private Member reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id")
    private Member reported;

    @Column(name = "content", columnDefinition = "VARCHAR(1000)")
    private String content;

}
