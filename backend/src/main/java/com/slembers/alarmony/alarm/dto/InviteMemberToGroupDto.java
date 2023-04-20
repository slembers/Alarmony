package com.slembers.alarmony.alarm.dto;

import com.slembers.alarmony.alarm.entity.Alarm;
import com.slembers.alarmony.member.entity.Member;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InviteMemberToGroupDto {

    private Member sender;
    private Alarm alarm;
    private Set<String> nicknames;

}
