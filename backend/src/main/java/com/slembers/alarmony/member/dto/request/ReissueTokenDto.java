package com.slembers.alarmony.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Builder
@ToString
@Getter
@Setter
public class ReissueTokenDto {

    @JsonProperty(value = "grant_type")
    private String grantType;
    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "refresh_token")
    private String refreshToken;


}
