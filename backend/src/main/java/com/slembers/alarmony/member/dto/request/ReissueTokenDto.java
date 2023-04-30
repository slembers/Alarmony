package com.slembers.alarmony.member.dto.request;

import lombok.Getter;
import lombok.ToString;


@ToString
@Getter
public class ReissueTokenDto {


    private String grantType;

    private String username;

    private String refreshToken;


}
