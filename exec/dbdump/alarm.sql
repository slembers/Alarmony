create table alarm
(
    alarm_id     bigint auto_increment
        primary key,
    alarm_date   varchar(7) default '0000000' null,
    content      varchar(255)                 null,
    sound_name   varchar(255)                 not null,
    sound_volume int        default 7         null,
    time         time                         not null,
    title        varchar(255)                 not null,
    vibrate      bit        default b'0'      null,
    host_id      bigint                       not null,
    constraint FKkvif2roqnwwset9utxyip4xap
        foreign key (host_id) references member (member_id)
);

INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (2, '1111111', null, 'Normal', 7, '17:04:00', '응가', true, 4);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (58, '0100000', null, 'Crescendo', 1, '00:55:00', '타요점심', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (59, '0100000', null, 'Piano', 1, '12:55:00', '타타요', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (60, '1111111', null, 'Normal', 1, '13:00:00', '하이', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (62, '0100000', null, 'Chicken', 1, '13:47:00', '헤ㄹ로', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (63, '0100000', null, 'Chicken', 1, '14:06:00', '구구', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (64, '0100000', null, 'Chicken', 3, '02:23:00', '하이루', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (67, '0100011', null, 'Normal', 2, '14:41:00', '나빌', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (68, '0100000', null, 'Crescendo', 2, '14:50:00', '임양원다이', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (70, '0100001', null, 'Normal', 2, '15:16:00', 'ㅎ', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (72, '0100000', null, 'Piano', 3, '15:24:00', 'ㅅㅅ', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (75, '0100000', null, 'Guitar', 1, '15:40:00', '아싸', false, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (78, '0100000', null, 'Chicken', 1, '18:06:00', '베', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (79, '0100000', null, 'Xylophone', 1, '18:05:00', '고나', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (80, '0100000', null, 'Xylophone', 1, '18:07:00', '배', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (82, '0100000', null, 'Piano', 1, '18:22:00', '마', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (83, '0100000', null, 'Normal', 1, '19:16:00', '구ㅉㅣ', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (84, '0100000', null, 'Normal', 3, '19:27:00', 'ㅅ', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (85, '0100000', null, 'SineLoop', 1, '19:39:00', 'ㅏㄹㅎ', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (86, '0100000', null, 'Normal', 1, '19:47:00', '사라ㅇ', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (87, '0100000', null, 'Normal', 2, '19:52:00', '로', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (89, '0100000', null, 'Normal', 0, '19:19:00', 'ㅏ자ㄱ', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (90, '0100000', null, 'Xylophone', 1, '19:57:00', '나루토', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (91, '0100000', null, 'Normal', 7, '20:37:00', '나', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (95, '0100000', null, 'Piano', 3, '21:48:00', '냐옹', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (96, '0100000', null, 'Normal', 9, '22:40:00', '응가맨', true, 4);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (99, '0100000', null, 'Normal', 1, '23:02:00', '나루미', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (101, '1111111', null, 'Normal', 7, '23:09:00', 'ㅇㅇ', true, 4);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (104, '0010000', null, 'Guitar', 1, '07:00:00', '미라클', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (106, '0010000', null, 'Chicken', 2, '07:29:00', 'ㅈㄱㆍ자', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (108, '0010000', null, 'Normal', 2, '09:26:00', '헬로', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (109, '0010000', null, 'Guitar', 0, '09:49:00', '고메', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (111, '0010000', null, 'Normal', 0, '10:15:00', '마', false, 3);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (112, '0010000', null, 'Normal', 0, '10:28:00', '하이', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (113, '0010000', null, 'Normal', 0, '10:35:00', '고고베베', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (115, '0010000', null, 'Normal', 0, '10:56:00', '헬로뉴월드', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (116, '0010000', null, 'Normal', 0, '11:28:00', '나옹', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (117, '0010000', null, 'Normal', 0, '11:31:00', '나나나', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (118, '0010000', null, 'Normal', 0, '11:41:00', '라', false, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (120, '0010000', null, 'Normal', 0, '11:59:00', '다구니', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (121, '0010000', null, 'Normal', 0, '12:07:00', 'ㄷ', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (122, '0010000', null, 'Normal', 1, '12:09:00', '나', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (124, '0010000', null, 'Normal', 0, '13:08:00', 'ㄴㄷ', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (125, '0010000', null, 'Normal', 0, '13:30:00', '가', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (126, '0010000', null, 'Normal', 1, '13:40:00', 'ㄷ', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (127, '0010000', null, 'Normal', 0, '13:43:00', '소', false, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (129, '0010000', null, 'Normal', 0, '13:51:00', '모짜', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (130, '0010000', null, 'Normal', 0, '14:13:00', 'ㅌ', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (137, '0010000', null, 'Normal', 0, '15:23:00', 'fgh', false, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (138, '0010000', null, 'Normal', 0, '15:25:00', '나', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (139, '0010000', null, 'Normal', 0, '15:27:00', '쇼', false, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (141, '0010000', null, 'Normal', 0, '15:30:00', '고나', false, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (146, '0010000', null, 'Guitar', 0, '16:31:00', '헬로', false, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (147, '0010000', null, 'Normal', 0, '16:33:00', '가챠', false, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (152, '1111111', null, 'Normal', 7, '17:11:00', 'dkdkd', true, 4);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (154, '0010000', null, 'Normal', 0, '17:17:00', '노노', false, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (167, '1111111', null, 'Xylophone', 7, '07:00:00', '알라모니 회의', true, 6);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (169, '0010000', null, 'Piano', 6, '21:04:00', 'ㄴㄷ', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (176, '1111111', null, 'Normal', 7, '23:31:00', 'tt', true, 7);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (177, '1111111', null, 'Normal', 7, '23:49:00', 'cxcx', true, 4);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (178, '1111111', null, 'Normal', 7, '00:26:00', '4', true, 6);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (179, '1111111', null, 'SineLoop', 15, '00:30:00', 'fish', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (180, '0000100', null, 'Normal', 0, '06:35:00', 'ㅅ', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (181, '1111111', null, 'SineLoop', 7, '07:00:00', '기상 1', true, 5);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (182, '1111111', null, 'SineLoop', 7, '07:30:00', '기상 2', true, 5);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (183, '1111111', null, 'SineLoop', 7, '08:00:00', '장덕동 기상 모임', true, 5);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (184, '1111111', null, 'Normal', 7, '14:00:00', '운동 모임', true, 5);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (185, '1010100', null, 'Normal', 7, '19:30:00', '알라모니 정규 회의', true, 5);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (186, '1111111', null, 'Normal', 7, '10:45:00', 'ㅇ', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (187, '1111111', null, 'Piano', 5, '10:57:00', '정처기스터디', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (188, '0001000', null, 'Piano', 4, '11:07:00', '정처기모임', true, 7);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (189, '0000011', null, 'Normal', 7, '08:08:00', '미라클주말', true, 7);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (190, '1111111', null, 'Piano', 7, '11:23:00', '싸피최고', true, 7);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (191, '1111111', null, 'Piano', 7, '11:27:00', '싸피최괴최고', true, 7);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (193, '1111111', null, 'Piano', 5, '11:33:00', '광주2반', true, 7);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (196, '1111111', null, 'Ukulele', 7, '20:01:00', '안녕', true, 9);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (197, '1011101', null, 'Ukulele', 7, '14:13:00', '아아', true, 5);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (198, '1111111', null, 'Normal', 7, '07:10:00', '기상', true, 5);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (199, '1111111', null, 'Normal', 7, '05:10:00', '1234', true, 6);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (201, '1111111', null, 'Normal', 7, '21:12:00', 'sd', true, 17);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (203, '0111001', '광주1반 미라클 모양입니다.', 'Normal', 7, '05:47:00', '하ㅏ이', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (204, '1111111', null, 'Normal', 7, '21:20:00', 'rhg', true, 17);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (207, '1111111', 'gm', 'Normal', 7, '06:08:00', 'ffh', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (208, '1111111', 'rty', 'Normal', 7, '06:08:00', 'qwe', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (209, '0001000', 'hello my name is tae yougn what is your name', 'Piano', 4, '06:11:00', 'hello', true, 16);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (210, '1111111', '나나나나나나나', 'Normal', 0, '15:14:00', '가요', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (212, '1111111', '박준영군 깨우는 알람입니다.', 'Piano', 1, '15:38:00', '박준영군 일어나', true, 7);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (213, '1111111', null, 'Normal', 7, '15:42:00', 'test', true, 17);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (214, '1111111', null, 'Normal', 7, '22:11:00', 'rty', true, 17);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (215, '1001111', null, 'Ukulele', 9, '22:33:00', '알람', true, 18);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (216, '1111111', null, 'Crescendo', 4, '16:03:00', '11', true, 5);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (217, '1111111', '', 'Crescendo', 7, '16:03:00', '아아', true, 7);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (218, '1111111', '해봅시다~~', 'Guitar', 7, '16:52:00', '테스트~', true, 19);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (220, '1111111', '자율 팀 알람입니다.', 'Normal', 7, '20:51:00', '싸피 알람', true, 5);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (221, '1111111', '재부팅실습', 'Normal', 5, '20:56:00', '딘딘파티', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (222, '0111100', '딘딘디틴', 'Normal', 4, '21:09:00', '딘딘딘', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (223, '0011111', 'ㅎㆍ호', 'Normal', 7, '21:10:00', '딘딘파티나잇', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (224, '1111111', '고고', 'Normal', 0, '21:10:00', '나나', true, 15);
INSERT INTO alarmony.alarm (alarm_id, alarm_date, content, sound_name, sound_volume, time, title, vibrate, host_id) VALUES (225, '1111111', '자율 프로젝트 알람입니다.', 'SineLoop', 7, '21:28:00', '싸피', true, 5);
