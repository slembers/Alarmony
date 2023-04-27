package com.slembers.alarmony.alarm.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ResponseInviteRequestDto {

    @JsonProperty(value = "accept")
    private Boolean accept;
}
