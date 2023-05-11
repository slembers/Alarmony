package com.slembers.alarmony.alarm.dto;

import com.slembers.alarmony.alarm.entity.AlertTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlertDto {

    private Long id;

    private String profileImg;

    private String content;

    private String type;

    public AlertDto(Long id, String profileImg, String content, AlertTypeEnum type) {
        this.id = id;
        this.profileImg = profileImg;
        this.content = content;
        this.type = type.name();
    }
}
