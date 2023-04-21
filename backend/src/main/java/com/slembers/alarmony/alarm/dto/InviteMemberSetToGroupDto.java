package com.slembers.alarmony.alarm.dto;

import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InviteMemberSetToGroupDto {

    private Long groupId;
    private Set<String> nicknames;

}
