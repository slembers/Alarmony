package com.slembers.alarmony.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberRankingDto {

    private String nickname;
    private String profileImg;
    private Float wakeUpAvg;

}
