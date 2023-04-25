package com.slembers.alarmony.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class AlertDto {

    @JsonProperty(value = "alert_id")
    private Long id;

    @JsonProperty(value = "profile_img")
    private String profileImg;

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "type")
    private String type;
}
