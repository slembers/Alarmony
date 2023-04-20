package com.slembers.alarmony.member.service;

import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.entity.Member;

public interface MemberService {

    /**
     * 아이디 중복체크
     **/
    CheckDuplicateDto checkForDuplicateId(String username);


    /**
     * 이메일 중복체크
     */
    CheckDuplicateDto checkForDuplicateEmail(String email);

    /**
     * 닉네임 중복 체크
     */
    CheckDuplicateDto checkForDuplicateNickName(String email);

    /**
     * username를 받으면 pk를 리턴
     */

    Member getMemberByUsername(String username);

    /**
     * nickname을 받으면 pk를 리턴
     */

    Member getMemberByNickName(String nickname);


}
