create table alert
(
    alert_id    bigint auto_increment
        primary key,
    content     varchar(255)                 not null,
    type        varchar(255) default 'BASIC' null,
    alarm_id    bigint                       null,
    receiver_id bigint                       not null,
    sender_id   bigint                       null,
    constraint FK9s2732xbtxku047ilp8j45hhq
        foreign key (sender_id) references member (member_id),
    constraint FKrcql3ic1gnwe0sme5911103bi
        foreign key (alarm_id) references alarm (alarm_id),
    constraint FKsyxsf5jv5g2nyjfu07g6nf1yd
        foreign key (receiver_id) references member (member_id)
);

INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (119, '''타요오'' 그룹이 삭제되었습니다.', 'DELETE', null, 3, null);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (120, '''아아'' 그룹이 삭제되었습니다.', 'DELETE', null, 3, null);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (126, '''감자'' 그룹이 삭제되었습니다.', 'DELETE', null, 3, null);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (185, '''베'' 그룹 초대입니다.', 'INVITE', 78, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (186, '''고나'' 그룹 초대입니다.', 'INVITE', 79, 3, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (188, '''배'' 그룹 초대입니다.', 'INVITE', 80, 3, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (194, '타요3님이 그룹 초대를 수락하셨습니다.', 'REPLY', 82, 16, 3);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (195, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 82, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (196, '''사라ㅇ'' 그룹 초대입니다.', 'INVITE', 86, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (197, '''로'' 그룹 초대입니다.', 'INVITE', 87, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (198, '''ㅏ자ㄱ'' 그룹 초대입니다.', 'INVITE', 89, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (201, '타요3님이 그룹 초대를 수락하셨습니다.', 'REPLY', 90, 15, 3);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (202, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 90, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (205, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 95, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (209, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 99, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (215, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 104, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (227, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 111, 3, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (228, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 111, 3, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (238, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 116, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (240, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 117, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (256, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 120, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (264, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 124, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (266, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 125, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (269, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 126, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (271, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 127, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (273, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 129, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (275, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 130, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (283, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 137, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (285, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 138, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (288, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 141, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (303, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 147, 15, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (305, 'swany0509님이 그룹 초대를 수락하셨습니다.', 'REPLY', 152, 4, 5);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (307, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 154, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (308, '''응가'' 그룹 초대입니다.', 'INVITE', 2, 3, 4);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (309, '''응가'' 그룹 초대입니다.', 'INVITE', 2, 16, 4);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (311, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 2, 4, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (324, '사암님이 그룹 초대를 수락하셨습니다.', 'REPLY', 167, 6, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (327, '''하이루'' 그룹 초대입니다.', 'INVITE', 64, 9, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (337, '''응가맨'' 그룹 초대입니다.', 'INVITE', 96, 14, 4);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (353, '''fish'' 그룹 초대입니다.', 'INVITE', 179, 3, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (354, '''fish'' 그룹 초대입니다.', 'INVITE', 179, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (357, 'fish님이 그룹 초대를 수락하셨습니다.', 'REPLY', 179, 15, 4);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (358, 'fish님이 그룹 초대를 수락하셨습니다.', 'REPLY', 179, 15, 4);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (359, '''tt'' 그룹 초대입니다.', 'INVITE', 176, 3, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (360, '''tt'' 그룹 초대입니다.', 'INVITE', 176, 16, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (361, '''tt'' 그룹 초대입니다.', 'INVITE', 176, 15, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (363, '''정처기스터디'' 그룹 초대입니다.', 'INVITE', 187, 16, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (364, '타요3님이 그룹 초대를 수락하셨습니다.', 'REPLY', 187, 15, 3);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (368, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 188, 7, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (369, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 188, 7, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (370, '타요3님이 그룹 초대를 수락하셨습니다.', 'REPLY', 188, 7, 3);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (374, '타요3님이 그룹 초대를 수락하셨습니다.', 'REPLY', 189, 7, 3);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (375, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 189, 7, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (376, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 189, 7, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (380, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 190, 7, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (381, '타요3님이 그룹 초대를 수락하셨습니다.', 'REPLY', 190, 7, 3);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (382, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 190, 7, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (383, '''싸피최괴최고'' 그룹 초대입니다.', 'INVITE', 191, 3, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (384, '''싸피최괴최고'' 그룹 초대입니다.', 'INVITE', 191, 16, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (386, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 191, 7, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (390, '타요1님이 그룹 초대를 수락하셨습니다.', 'REPLY', 193, 7, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (391, '타요2님이 그룹 초대를 수락하셨습니다.', 'REPLY', 193, 7, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (392, '타요3님이 그룹 초대를 수락하셨습니다.', 'REPLY', 193, 7, 3);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (394, 'swany0509님이 그룹 초대를 수락하셨습니다.', 'REPLY', 196, 9, 5);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (395, '''아아'' 그룹 초대입니다.', 'INVITE', 197, 6, 5);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (396, '''기상'' 그룹 초대입니다.', 'INVITE', 198, 6, 5);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (397, '''1234'' 그룹 초대입니다.', 'INVITE', 199, 5, 6);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (402, '''sd'' 그룹 초대입니다.', 'INVITE', 201, 6, 17);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (403, '''sd'' 그룹 초대입니다.', 'INVITE', 201, 5, 17);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (404, '''sd'' 그룹 초대입니다.', 'INVITE', 201, 14, 17);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (405, '''하ㅏ이'' 그룹 초대입니다.', 'INVITE', 203, 6, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (406, '''하ㅏ이'' 그룹 초대입니다.', 'INVITE', 203, 5, 16);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (407, '''rhg'' 그룹 초대입니다.', 'INVITE', 204, 4, 17);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (408, '''rhg'' 그룹 초대입니다.', 'INVITE', 204, 5, 17);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (409, '''rhg'' 그룹 초대입니다.', 'INVITE', 204, 14, 17);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (422, '''알람'' 그룹 초대입니다.', 'INVITE', 215, 6, 18);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (424, '''알람'' 그룹 초대입니다.', 'INVITE', 215, 5, 18);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (427, '''운동 모임'' 그룹 초대입니다.', 'INVITE', 184, 6, 5);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (431, 'swany0509님이 그룹 초대를 수락하셨습니다.', 'REPLY', 218, 19, 5);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (432, '''싸피최괴최고'' 그룹 초대입니다.', 'INVITE', 191, 3, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (433, '''싸피최괴최고'' 그룹 초대입니다.', 'INVITE', 191, 16, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (434, 'rerere님이 그룹 초대를 거절하셨습니다.', 'REPLY', 215, 18, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (435, 'rerere님이 그룹 초대를 거절하셨습니다.', 'REPLY', 213, 17, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (438, '''싸피 알람'' 그룹 초대입니다.', 'INVITE', 220, 6, 5);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (439, '''딘딘파티'' 그룹 초대입니다.', 'INVITE', 221, 7, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (440, '''딘딘딘'' 그룹 초대입니다.', 'INVITE', 222, 7, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (441, '''딘딘파티나잇'' 그룹 초대입니다.', 'INVITE', 223, 7, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (443, '''나나'' 그룹 초대입니다.', 'INVITE', 224, 5, 15);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (444, '싸피_박준영님이 그룹 초대를 수락하셨습니다.', 'REPLY', 224, 15, 7);
INSERT INTO alarmony.alert (alert_id, content, type, alarm_id, receiver_id, sender_id) VALUES (445, '''싸피'' 그룹 초대입니다.', 'INVITE', 225, 7, 5);
