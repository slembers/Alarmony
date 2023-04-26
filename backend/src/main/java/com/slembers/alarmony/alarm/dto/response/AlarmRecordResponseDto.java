package com.slembers.alarmony.alarm.dto.response;

import com.slembers.alarmony.member.dto.MemberInfoDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmRecordResponseDto {

    private List<MemberInfoDto> alarmOn;
    private List<MemberInfoDto> alarmOff;

}
