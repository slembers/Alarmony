package com.slembers.alarmony.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PutRegistrationTokenRequestDto {

    private String registrationToken;
}
