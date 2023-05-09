package com.slembers.alarmony.member.service;

import com.slembers.alarmony.global.amazons3.AmazonS3Util;
import com.slembers.alarmony.global.dto.MessageResponseDto;
import com.slembers.alarmony.global.execption.CustomException;
import com.slembers.alarmony.global.security.jwt.JwtTokenProvider;
import com.slembers.alarmony.global.redis.service.RedisUtil;
import com.slembers.alarmony.member.dto.ChangePasswordDto;
import com.slembers.alarmony.member.dto.MemberInfoDto;
import com.slembers.alarmony.member.dto.request.FindMemberIdDto;
import com.slembers.alarmony.member.dto.request.FindPasswordDto;
import com.slembers.alarmony.member.dto.request.ReissueTokenDto;
import com.slembers.alarmony.member.dto.request.SignUpDto;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.dto.response.MemberResponseDto;
import com.slembers.alarmony.member.dto.response.TokenResponseDto;
import com.slembers.alarmony.member.entity.AuthorityEnum;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.exception.MemberErrorCode;
import com.slembers.alarmony.member.repository.MemberRepository;
import com.slembers.alarmony.report.dto.ModifiedMemberInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;

    private final EmailVerifyService emailVerifyService;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final RedisUtil redisUtil;

    private final AmazonS3Util amazonS3Util;


    /**
     * 회원가입
     *
     * @param signUpDto 회원가입 정보
     */
    @Transactional
    @Override
    public void signUp(SignUpDto signUpDto) {

        checkDuplicatedField(signUpDto);

        Member member = modelMapper.map(signUpDto, Member.class);


        member.encodePassword(passwordEncoder);

        emailVerifyService.sendVerificationMail(member.getUsername(), member.getEmail());

        memberRepository.save(member);

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

        //TODO: 얘 리펙토링 필요한거 같음 리턴을 dto로 하면 안될듯
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
     * 회원 가입시 발생하는 중복 체크
     *
     * @param signUpDto: 회원 가입 정보
     */

    private void checkDuplicatedField(SignUpDto signUpDto) {

        //아이디 중복 체크
        if (checkForDuplicateId(signUpDto.getUsername()).isDuplicated())
            throw new CustomException(MemberErrorCode.ID_DUPLICATED);
        //닉네임 중복 체크
        if (checkForDuplicateNickname(signUpDto.getNickname()).isDuplicated())
            throw new CustomException(MemberErrorCode.NICKNAME_DUPLICATED);
        //이메일 중복 체크
        if (checkForDuplicateEmail(signUpDto.getEmail()).isDuplicated())
            throw new CustomException(MemberErrorCode.EMAIL_DUPLICATED);

    }

    @Override
    public TokenResponseDto reissueToken(ReissueTokenDto reissueTokenDto) {

        //예외처리
        jwtTokenProvider.validRefreshToken(reissueTokenDto.getRefreshToken());

        String redisRefreshToken = redisUtil.getData("Refresh:" + reissueTokenDto.getUsername());

        Member member = memberRepository.findByUsername(reissueTokenDto.getUsername()).orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (redisRefreshToken.equals(redisRefreshToken)) { //일치할때만 재발급

            String accessToken = jwtTokenProvider.generateAccessToken(member);
            String refreshToken = jwtTokenProvider.generateRefreshToken(member);

            return new TokenResponseDto("bearer", accessToken, refreshToken);

        } else {  //일치하지 않으면
            log.error("[Refresh Controller] Refresh Token값이 일치하지 않습니다.");
        }

        return null;
    }

    @Override
    public void putRegistrationToken(String username, String registrationToken) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (registrationToken == null || registrationToken.length() == 0)
            throw new CustomException(MemberErrorCode.MEMBER_REGISTRATION_TOKEN_WRONG);

        try {
            member.modifyToken(registrationToken);
            memberRepository.save(member);
        } catch (Exception e) {
            throw new CustomException(MemberErrorCode.MEMBER_REGISTRATION_TOKEN_WRONG);
        }
    }

    /**
     * 아이디 찾기
     *
     * @param findMemberIdDto 회원 이메일
     */

    @Override
    public MessageResponseDto findMemberId(FindMemberIdDto findMemberIdDto) {

        Member member = memberRepository.findMemberByEmail(findMemberIdDto.getEmail())
                .orElseThrow(() -> new CustomException(MemberErrorCode.EMAIL_NOT_FOUND));

        Map<String, String> values = new HashMap<>();
        values.put("username", member.getUsername());

        emailVerifyService.sendTemplateEmail("알라모니 아이디 찾기", findMemberIdDto.getEmail(), "FindId", values);

        return new MessageResponseDto(findMemberIdDto.getEmail() + "로 아이디 찾기 안내 메일을 전송하였습니다.");
    }

    /**
     * 비밀번호 찾기
     */
    @Transactional
    @Override
    public void findMemberPassword(FindPasswordDto findPasswordDto) {

        Member member = memberRepository.findMemberByUsernameAndEmail(findPasswordDto.getUsername(), findPasswordDto.getEmail())
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        //임시비밀번호 발급
        String tempPassword = getTempPassword();

        Map<String, String> values = new HashMap<>();
        values.put("username", member.getUsername());
        values.put("temp_password", tempPassword);

        //이메일 보내기
        emailVerifyService.sendTemplateEmail("알라모니 임시 비밀번호 발급 안내", findPasswordDto.getEmail(), "FindPw", values);

        //이메일 전송 완료후 임시비밀번호로 변경
        member.changePassword(tempPassword);
        member.encodePassword(passwordEncoder);

        memberRepository.save(member);

    }

    /**
     * 랜덤함수로 임시비밀번호 구문 만들기
     */
    private String getTempPassword() {

        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        StringBuilder str = new StringBuilder();
        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str.append(charSet[idx]);
        }
        return str.toString();
    }

    /**
     * 회원 정보 조회하기
     */
    @Override
    public MemberResponseDto getMemberInfo(String username) {


        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        return modelMapper.map(member, MemberResponseDto.class);
    }

    /**
     * 회원 탈퇴
     */
    @Override
    @Transactional
    public MessageResponseDto deleteMember(String username) {


        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        //권한 비활성화로 변경
        member.modifyAuthority(AuthorityEnum.ROLE_WITHDRAWAL);

        memberRepository.save(member);

        return new MessageResponseDto("회원 탈퇴 완료");
    }

    /**
     * 회원 정보 수정
     */

    @Transactional
    public MemberInfoDto modifyMemberInfo(String username, ModifiedMemberInfoDto modifiedMemberInfoDto) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        String key = "";
        String url = "";

        //변경할 프로필 사진을 제대로 받아온 경우에만 실행
        if (modifiedMemberInfoDto.getImgProfileFile() != null) {

            try {
                key = amazonS3Util.upload(modifiedMemberInfoDto.getImgProfileFile(), "member");
            } catch (IOException e) {
                log.error("S3에 이미지 저장 실패");
                throw new CustomException(MemberErrorCode.AMAZONS3_ERROR);
            }
            //s3에 업로드하여 이미지에 대한 key값을 받아온다.
            url = amazonS3Util.getFileUrl(key);

            //이미지 프로필 키가 널이 아닐경우에만 s3에서 기존 프로필 이미지를 삭제시킨다.
            if (member.getProfileKey() != null) {
                amazonS3Util.delete(member.getProfileKey());
            }

            member.changeProfileImg(url);
            member.changeProfileKey(key);


        }

        member.changeNickname(modifiedMemberInfoDto.getNickname());
        memberRepository.save(member);
        return new MemberInfoDto(modifiedMemberInfoDto.getNickname(), url);


    }

    /**
     * 비밀번호 변경
     */
    @Override
    @Transactional
    public MessageResponseDto changePassword(String username, ChangePasswordDto changePasswordDto) {

        //가존 비밀번호와 일치하는지 확인

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), member.getPassword())) {
            throw new CustomException(MemberErrorCode.PASSWORD_NOT_VALID);
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())) {
            throw new CustomException(MemberErrorCode.CHANGE_PASSWORD_NOT_SAME);
        }

        //변경할 수 있다면 비밀번호를 변경한다.
        member.changePassword(changePasswordDto.getNewPassword());
        member.encodePassword(passwordEncoder);

        memberRepository.save(member);


        return new MessageResponseDto("비밀번호 변경을 완료하였습니다.");
    }
}
