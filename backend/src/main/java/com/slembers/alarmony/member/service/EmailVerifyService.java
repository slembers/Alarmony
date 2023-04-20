package com.slembers.alarmony.member.service;

import com.slembers.alarmony.member.dto.vo.MemberVerificationDto;

public interface EmailVerifyService {


    /**
     * 이메일 보내기
     * @param to
     * @param sub
     * @param text
     */
     void sendMail(String to,String sub, String text);


    /**
     * 인증 이메일 보내기
     * @param memberVerificationDto
     */
    public void sendVerificationMail(MemberVerificationDto memberVerificationDto) ;


    /**
     * 이메일 인증 확인하기
     * @param key
     */
    void verifyEmail(String key);


}
