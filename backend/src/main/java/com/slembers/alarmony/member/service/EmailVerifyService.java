package com.slembers.alarmony.member.service;

public interface EmailVerifyService {


    /**
     * 이메일 보내기
     *
     * @param to   받는 사람의 이메일
     * @param sub  이메일 제목
     * @param text 이메일 내용
     */
    void sendMail(String to, String sub, String text);

    /**
     * 인증 이메일 보내기
     *
     * @param username 유저 아이디
     * @param email    이메일
     **/
    public void sendVerificationMail(String username, String email);

    /**
     * 이메일 인증 확인하기
     *
     * @param key 이메일 인증 발급 키값
     */
    void verifyEmail(String key);


}
