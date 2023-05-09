package com.slembers.alarmony.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ModifiedMemberInfoDto
{
    private String nickname;
    private MultipartFile imgProfileFile;

}
