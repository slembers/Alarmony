package com.slembers.alarmony.member.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
public class ReissueTokenDto {

    private String grantType;
    private String username;

    private String refreshToken;


}
