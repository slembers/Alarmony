package com.slembers.alarmony.alarm.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseInviteRequestDto {

    @NotNull(message = "수락 여부는 필수입니다.")
    private Boolean accept;
}
