package com.slembers.alarmony.member.service;

import com.slembers.alarmony.global.redis.service.RedisUtil;
import com.slembers.alarmony.member.dto.vo.MemberVerificationDto;
import com.slembers.alarmony.member.entity.AuthorityEnum;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmailVerifyServiceImpl implements EmailVerifyService{


    private  final JavaMailSender emailSender;
    private  final RedisUtil redisUtil;

    private  final MemberRepository memberRepository;

    private final MemberService memberService;

    @Override
    public void sendMail(String to, String sub, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(sub);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendVerificationMail(MemberVerificationDto memberVerificationDto) {

        String verificationLink = "http://localhost:5000/api/members/vertify";
        UUID uuid = UUID.randomUUID();
        redisUtil.setDataExpire(uuid.toString(),memberVerificationDto.getUsername(), 60 * 30L);
        sendMail(memberVerificationDto.getEmail() , "알라모니 회원가입 인증 메일",verificationLink + uuid);

    }

    @Override
    public void verifyEmail(String key) {
        String username = redisUtil.getData(key);
        Member member = memberRepository.findByUsername(username).orElseThrow();
        memberService.modifyUserRole(member, AuthorityEnum.ROLE_USER);

    }
}
