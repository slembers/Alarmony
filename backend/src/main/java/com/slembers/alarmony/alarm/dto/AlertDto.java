package com.slembers.alarmony.alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.slembers.alarmony.alarm.entity.AlertTypeEnum;

public class AlertDto {

    @JsonProperty(value = "alert_id")
    private Long id;

    @JsonProperty(value = "profile_img")
    private String profileImg;

    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "type")
    private String type;

    public AlertDto(Long id, String profileImg, String content, AlertTypeEnum type) {
        this.id = id;
        this.profileImg = profileImg;
        this.content = content;
        this.type = type.name();
    }
}
