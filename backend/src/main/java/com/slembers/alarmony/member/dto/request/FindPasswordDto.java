package com.slembers.alarmony.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FindPasswordDto {
    @JsonProperty(value = "username")
    private String username;
    @JsonProperty(value = "email")
    private String email;

}
