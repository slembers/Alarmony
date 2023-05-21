package com.slembers.alarmony.alarm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeGroupHostRequestDto {

    @NotNull(message = "닉네임을 입력해주세요.")
    private String newHost;
}
