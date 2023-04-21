package com.slembers.alarmony.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberInfoDto {

    private String nickname;
    private String memberImg;

}
