package com.slembers.alarmony.alarm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CompareRegistTokenRequestDto {
    @NotNull(message = "현재 토큰이 없습니다.")
    private String registToken;
}
