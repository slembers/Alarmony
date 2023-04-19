package com.slembers.alarmony.member.service;

public interface MemberService {

    /**
     * 아이디 중복체크
     **/
    boolean checkForDuplicateId(String username);


    /**
     * username를 받으면 pk를 리턴
     */

    Long getMemberByUsername(String username);

    /**
     * nickname을 받으면 pk를 리턴
     */

    Long getMemberByNickName(String nickname);



}
