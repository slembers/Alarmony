package com.slembers.alarmony.member.service;

import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.jwt.JwtTokenProvider;
import com.slembers.alarmony.global.jwt.RefreshToken;
import com.slembers.alarmony.global.jwt.dto.TokenDto;
import com.slembers.alarmony.global.redis.repository.RefreshTokenRepository;
import com.slembers.alarmony.member.dto.LoginDto;
import com.slembers.alarmony.member.dto.request.SignUpDto;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    private final EmailVerifyService emailVerifyService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final RefreshTokenRepository refreshTokenRepository;

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
    public ResponseEntity<String> login(LoginDto loginDto , HttpServletResponse response) {

        //로그인 검증
        LOGIN_VALIDATE(loginDto);

        log.info("검증완료");

         // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword());

        log.info("정보"+ loginDto.getUsername() + " "+loginDto.getPassword());
        log.info("Authentication 객체 생성"+ authenticationToken);

        log.info("Authentication 객체 생성");
        //실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        //authenticationToken으로부터 사용자 이름과 패스워드를 추출하여 실제 사용자 정보와 일치하는지 확인합니다.

        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        }catch (Exception e){
            log.info(e.getMessage());
        }
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("Authentication 객체 생성2"+authenticationManagerBuilder.getObject().authenticate(authenticationToken));
        SecurityContextHolder.getContext().setAuthentication(authentication);



        //accessToken 발급
        String accessToken = jwtTokenProvider.createAccessToken(authentication);
        log.info("accessToken 발급");

        //refreshToken 발급
        String refreshToken = jwtTokenProvider.createRefreshToken(loginDto.getUsername());
        log.info("refreshToken 발급");

        log.info("accessToken :" + accessToken);
        log.info("refreshToken :" + refreshToken);

        //redis에 refreshToken 저장
        refreshTokenRepository.save(RefreshToken.builder().
                username(authentication.getName()).authorities(authentication.getAuthorities()).refreshToken(refreshToken)
                .build());


        //아이디 정보로 Token생성
        TokenDto tokenDto = new TokenDto(accessToken,refreshToken);
        //헤더에 저장
        setHeader(response, tokenDto);

       return ResponseEntity.ok().body("로그인 성공");
    }

    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtTokenProvider.ACCESS_TOKEN, tokenDto.getAccessToken());
        response.addHeader(JwtTokenProvider.REFRESH_TOKEN, tokenDto.getRefreshToken());

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

    /**
     * 로그인 검증 확인
     */
    private void LOGIN_VALIDATE(LoginDto loginDto){
        //존재하지 않는 회원으로 로그인을 시도할경우 예외처리 발생
        Member member = memberRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        //비밀번호가 일치하는지 확인한다.
        boolean isMatch = passwordEncoder.matches(loginDto.getPassword(), member.getPassword());
        if(!isMatch)  throw new BadCredentialsException("아이디 또는 비밀번호를 확인후 다시 로그인하여 주시기 바랍니다.");

    }


}
