package com.slembers.alarmony.member.service;

import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.redis.service.RedisUtil;
import com.slembers.alarmony.global.redis.service.exception.RedisErrorCode;
import com.slembers.alarmony.global.util.UrlInfo;
import com.slembers.alarmony.member.entity.AuthorityEnum;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.EmailVerifyErrorCode;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;
    private final UrlInfo urlInfo;
    private final SpringTemplateEngine templateEngine;


    @Async
    public void sendTemplateEmail(String title, String to, String templateName, Map<String, String> values) {

        try {

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);


            helper.setSubject(title);
            helper.setTo(to);

            Context context = new Context();
            values.forEach(context::setVariable);

            String html = templateEngine.process(templateName, context);
            helper.setText(html, true);

            helper.addInline("alarmonyLogo", new ClassPathResource("static/image/alarmonyLogo.png"));

            emailSender.send(message);

        } catch (MessagingException e) {
            log.error("[EmailService] 메세지 작성 에 문제 발생: " + e.getMessage());
            throw new CustomException(EmailVerifyErrorCode.EMAIL_INTERNAL_ERROR);

        } catch (MailException e) {
            log.error("[EmailService] 메일 보내기에 문제 발생: " + e.getMessage());
            throw new CustomException(EmailVerifyErrorCode.EMAIL_INTERNAL_ERROR);
        }

    }

    @Override
    @Async
    public void sendSignUpVerificationMail(String username, String email) {

        UUID uuid = UUID.randomUUID();
        String verificationApi = urlInfo.getAlarmonyUrl() + "/api/members/verify/" + uuid;

        setMemberVerifyCodeToRedis(uuid, username);

        Map<String, String> values = new HashMap<>();
        values.put("username", username);
        values.put("verify_link", verificationApi);

        sendTemplateEmail("알라모니 회원가입 인증 메일", email, "Verify", values);

    }

    private void setMemberVerifyCodeToRedis(UUID uuid, String username){

        try {
            redisUtil.setData(uuid.toString(), username);
        } catch (Exception e) {
            log.error("[Redis Error] : redis 문제 발생 " + e.getMessage());
            throw new CustomException(RedisErrorCode.REDIS_ERROR_CODE);
        }

    }

    @Override
    @Transactional
    public void confirmSignUp(String key) {

        String username = redisUtil.getData(key);

        if (username != null) {
            modifyAuthorityAccess(username);
            redisUtil.deleteData(key);
        } else {
            throw new CustomException(EmailVerifyErrorCode.VERIFY_KEY_NOT_FOUND);
        }

    }

    @Transactional
    public void modifyAuthorityAccess(String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.updateAuthority(AuthorityEnum.ROLE_USER);

    }
}
