package com.slembers.alarmony.alarm.dto.request;

import java.util.Set;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InviteMemberToGroupRequestDto {

    @NotNull(message = "초대할 멤버 목록은 필수입니다.")
    private Set<String> members;

}
