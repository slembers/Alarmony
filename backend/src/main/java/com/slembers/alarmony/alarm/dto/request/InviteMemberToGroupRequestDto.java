package com.slembers.alarmony.alarm.dto.request;

import java.util.Set;
import lombok.Getter;

@Getter
public class InviteMemberToGroupRequestDto {

    private Set<String> members;

}
