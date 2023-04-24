package com.slembers.alarmony.member.service;

import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.jwt.JwtProvider;
import com.slembers.alarmony.global.jwt.dto.TokenDto;
import com.slembers.alarmony.member.dto.LoginDto;
import com.slembers.alarmony.member.dto.request.SignUpDto;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    private final EmailVerifyService emailVerifyService;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;


    /**
     * 회원가입
     *
     * @param signUpDto 회원가입 정보
     * @return 성공/실패
     */
    @Transactional
    @Override
    public boolean signUp(SignUpDto signUpDto) {


        //아이디 중복 체크
        if (checkForDuplicateId(signUpDto.getUsername()).isDuplicated())
            throw new CustomException(MemberErrorCode.ID_DUPLICATED);
        //닉네임 중복 체크
        if (checkForDuplicateNickname(signUpDto.getNickname()).isDuplicated())
            throw new CustomException(MemberErrorCode.NICKNAME_DUPLICATED);
        //이메일 중복 체크
        if (checkForDuplicateEmail(signUpDto.getEmail()).isDuplicated())
            throw new CustomException(MemberErrorCode.EMAIL_DUPLICATED);

        Member member = modelMapper.map(signUpDto, Member.class);
        //비밀번호 암호화
        member.encodePassword(passwordEncoder);

        //저장이 잘 완료 되었다면 인증 메일을 전송한다.
        emailVerifyService.sendVerificationMail(member.getUsername(), member.getEmail());

        memberRepository.save(member);
        return true;
    }

    /**
     * 로그인
     *
     * @param loginDto 로그인 정보
     */
    @Override
    public void login(LoginDto loginDto , HttpServletResponse response) {
        //로그인 하는 유저ID가 존재하지 않는 경우 예외 발생
        Member member = memberRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        //비밀번호가 일치하는지 확인한다.
        boolean isMatch = passwordEncoder.matches(loginDto.getPassword(), member.getPassword());
        if(!isMatch)  throw  new BadCredentialsException("아이디 또는 비밀번호를 확인후 다시 로그인하여 주시기 바랍니다.");

        //정상 적으로 로그인이 되었다면 Access Token을 생성한다.
        //아이디 정보로 Token생성
        TokenDto tokenDto = jwtProvider.createAllToken(loginDto.getUsername());

        setHeader(response, tokenDto);

        //return new GlobalResDto("Success Login", HttpStatus.OK.value());
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtProvider.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtProvider.REFRESH_TOKEN, tokenDto.getRefreshToken());

    }
    /**
     * 아이디 중복체크
     *
     * @param username 유저 아이디
     * @return 존재여부
     **/

    @Override
    public CheckDuplicateDto checkForDuplicateId(String username) {

        return CheckDuplicateDto.builder().isDuplicated(memberRepository.existsByUsername(username)).build();

    }

    /**
     * 이메일 중복 체크
     *
     * @param email :이메일주소
     * @return 존재여부
     */
    @Override
    public CheckDuplicateDto checkForDuplicateEmail(String email) {
        return CheckDuplicateDto.builder().isDuplicated(memberRepository.existsByEmail(email)).build();
    }


    /**
     * 닉네임 중복 체크
     *
     * @param nickname : 닉네임
     * @return 존재여부
     */
    @Override
    public CheckDuplicateDto checkForDuplicateNickname(String nickname) {
        return CheckDuplicateDto.builder().isDuplicated(memberRepository.existsByNickname(nickname)).build();
    }


}
