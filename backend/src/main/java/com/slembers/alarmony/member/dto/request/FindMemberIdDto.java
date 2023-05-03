package com.slembers.alarmony.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FindMemberIdDto {

    @JsonProperty(value = "email")
    private String email;
}
