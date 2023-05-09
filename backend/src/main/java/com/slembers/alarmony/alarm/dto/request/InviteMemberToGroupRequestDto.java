package com.slembers.alarmony.alarm.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class InviteMemberToGroupRequestDto {

    @NotNull(message = "초대할 멤버 목록은 필수입니다.")
    @JsonProperty(value = "members")
    private Set<String> members;

}
