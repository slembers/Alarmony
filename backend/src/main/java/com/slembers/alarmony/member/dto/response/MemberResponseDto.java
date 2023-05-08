package com.slembers.alarmony.member.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MemberResponseDto {

    private String username;
    private String nickname;
    private String profileImg;
    private String email;
}
