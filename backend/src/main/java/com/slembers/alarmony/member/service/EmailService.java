package com.slembers.alarmony.member.service;

import java.util.Map;

public interface EmailService {


    /**
     * 회원 가입 인증 이메일 보내기
     *
     * @param username 유저 아이디
     * @param email    이메일
     **/
    void sendSignUpVerificationMail(String username, String email);

    /**
     * 이메일 인증 확인하기
     *
     * @param key 이메일 인증 발급 키값
     */
    void confirmSignUp(String key);

    void sendTemplateEmail(String title, String to, String templateName, Map<String, String> values);

}
