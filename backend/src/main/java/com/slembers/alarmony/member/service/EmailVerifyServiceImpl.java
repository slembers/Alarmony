package com.slembers.alarmony.member.service;

import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.redis.service.RedisUtil;
import com.slembers.alarmony.global.redis.service.exception.RedisErrorCode;
import com.slembers.alarmony.global.util.UrlInfo;
import com.slembers.alarmony.member.dto.vo.MemberVerificationDto;
import com.slembers.alarmony.member.entity.AuthorityEnum;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.EmailVerifyErrorCode;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.naming.ldap.Rdn;
import javax.transaction.Transactional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerifyServiceImpl implements EmailVerifyService {


    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;
    private final UrlInfo urlInfo;

    /**
     * 이메일 보내기
     *
     * @param to   받는 사람의 이메일
     * @param sub  이메일 제목
     * @param text 이메일 내용
     */
    @Override
    public void sendMail(String to, String sub, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(sub);
        message.setText(text);
        emailSender.send(message);
    }

    /**
     * 인증 이메일 보내기
     *
     * @param username 유저 아이디
     * @param email    이메일
     **/
    @Override
    public void sendVerificationMail(String username, String email) {
        String verificationLink = urlInfo.getAlarmonyUrl() + "/api/members/verify";

        UUID uuid = UUID.randomUUID();
        try {
            redisUtil.setData(uuid.toString(), username);
        } catch (Exception e) {
            // 인증 실패 예외 처리
            log.error("Redis에 문제 발생 " + e.getMessage());
            throw new CustomException(RedisErrorCode.REDIS_ERROR_CODE);
        }

        try {
            sendMail(email, "알라모니 회원가입 인증 메일", verificationLink + "/" + uuid);
            log.info(email + " 로 이메일 전송 완료");
        } catch (Exception e) {
            // 인증 실패 예외 처리
            log.error("이메일 전송 실패 " + e.getMessage());
            throw new CustomException(EmailVerifyErrorCode.EMAIL_INTERNAL_ERROR);
        }


    }

    /**
     * 인증 메일 요청을 처리한다.
     *
     * @param key : 인증 토큰 값
     */

    @Transactional
    @Override
    public void verifyEmail(String key) {

        String username = redisUtil.getData(key);
        log.info("[회원가입 인증을 하려고하는 회원] : " + username);
        if (username != null) {
            modifyAuthorityAccess(username);
            redisUtil.deleteData(key);
        } else {
            throw new CustomException(EmailVerifyErrorCode.VERIFY_KEY_NOT_FOUND);
        }
    }

    /**
     * 회원의 권한을 인증완료한 회원권한으로 수정한다.
     *
     * @param username 회원아이디
     */
    @Transactional
    public void modifyAuthorityAccess(String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.modifyAuthority(AuthorityEnum.ROLE_USER);
        memberRepository.save(member);
        log.info(username + "의 ROLE_USER로 권한 변경");

    }
}
