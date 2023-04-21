package com.slembers.alarmony.member.dto.vo;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MemberVerificationDto {

    private String username;
    private String email;


    public MemberVerificationDto(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
