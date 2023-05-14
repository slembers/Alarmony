package com.slembers.alarmony.member.service;

import com.slembers.alarmony.member.dto.ChangePasswordDto;
import com.slembers.alarmony.member.dto.MemberInfoDto;
import com.slembers.alarmony.member.dto.request.FindMemberIdDto;
import com.slembers.alarmony.member.dto.request.FindPasswordDto;
import com.slembers.alarmony.member.dto.request.ReissueTokenDto;
import com.slembers.alarmony.member.dto.request.SignUpDto;
import com.slembers.alarmony.member.dto.response.*;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.report.dto.ModifiedMemberInfoDto;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;


public interface MemberService {

    /**
     * 회원 가입
     */
    void signUp(SignUpDto signUpDto);

    /**
     * 아이디 중복체크
     **/
    CheckDuplicateDto checkForDuplicateId(String username);


    /**
     * 이메일 중복체크
     */
    CheckDuplicateDto checkForDuplicateEmail(String email);

    /**
     * 닉네임 중복 체크
     */
    CheckDuplicateDto checkForDuplicateNickname(String nickname);


    /**
     * 토큰 재발급
     */

    TokenResponseDto reissueToken(ReissueTokenDto reissueTokenDto);

    void putRegistrationToken(String username, String registrationToken);

    /**
     * 아이디 찾기
     */

    void findMemberId (FindMemberIdDto findMemberIdDto) throws MessagingException;

    /**
     * 비밀 번호 찾기
     */

    void findMemberPassword(FindPasswordDto findPasswordDto);

    /**
     * 회원 정보 조회하기
     */

    MemberResponseDto getMemberInfo(String username);

    /**
     *  회원 탈퇴
     */
    void deleteMember(String username);


    /**
     * 회원 정보 변경
     */
    MemberInfoDto modifyMemberInfo(String username , ModifiedMemberInfoDto modifiedMemberInfoDto) throws IOException;

    /**
     * 비밀 번호 변경
     */
    void changePassword(String username,ChangePasswordDto changePasswordDto);

    Member findMemberByUsername(String username);

    Member findMemberByNickName(String nickname);

    /**
     * 이미지 변경
     */
    ImageResponseDto modifyMemberImage(String username, MultipartFile modifyImage);

    /**
     * 닉네임 변경
     */
    NicknameResponseDto modifyMemberNickname(String currentUsername, String changeName);
}
