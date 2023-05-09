package com.slembers.alarmony.alarm.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ResponseInviteRequestDto {

    @NotNull(message = "수락 여부는 필수입니다.")
    @JsonProperty(value = "accept")
    private Boolean accept;
}
