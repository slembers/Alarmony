package com.slembers.alarmony.member.dto.response;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class IdCheckResponseDto {
    private boolean passed;
}
