package com.slembers.alarmony.member.service;

import com.slembers.alarmony.member.dto.response.IdCheckResponseDto;
import com.slembers.alarmony.member.entity.Member;

public interface MemberService {

    /**
     * 아이디 중복체크
     **/
    IdCheckResponseDto checkForDuplicateId(String username);


    /**
     * username를 받으면 pk를 리턴
     */

    Member getMemberByUsername(String username);

    /**
     * nickname을 받으면 pk를 리턴
     */

    Member getMemberByNickName(String nickname);



}
