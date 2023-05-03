package com.slembers.alarmony.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class PutRegistrationTokenRequestDto {

    @JsonProperty(value = "registration_token")
    private String registrationToken;
}
