create table report_record
(
    report_record_id bigint auto_increment
        primary key,
    content          varchar(1000)                 null,
    report_type      varchar(20) default 'APP_BUG' not null,
    reported_id      bigint                        null,
    reporter_id      bigint                        not null,
    constraint FK4ie203p4l8h4k63qubdx4o8yo
        foreign key (reporter_id) references member (member_id),
    constraint FKoegcfvgp9nlo5tvps5g9t0pnq
        foreign key (reported_id) references member (member_id)
);

INSERT INTO alarmony.report_record (report_record_id, content, report_type, reported_id, reporter_id) VALUES (1, '내용', 'USER_REPORT', 7, 5);
INSERT INTO alarmony.report_record (report_record_id, content, report_type, reported_id, reporter_id) VALUES (2, '내용', 'USER_REPORT', 7, 5);
INSERT INTO alarmony.report_record (report_record_id, content, report_type, reported_id, reporter_id) VALUES (3, '내용', 'USER_REPORT', 7, 5);
INSERT INTO alarmony.report_record (report_record_id, content, report_type, reported_id, reporter_id) VALUES (4, '내용', 'USER_REPORT', 7, 5);
INSERT INTO alarmony.report_record (report_record_id, content, report_type, reported_id, reporter_id) VALUES (5, '내용', 'USER_REPORT', 7, 5);
INSERT INTO alarmony.report_record (report_record_id, content, report_type, reported_id, reporter_id) VALUES (6, '내용', 'USER_REPORT', 7, 5);
INSERT INTO alarmony.report_record (report_record_id, content, report_type, reported_id, reporter_id) VALUES (7, '내용', 'USER_REPORT', 7, 5);
