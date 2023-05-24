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


    void findMemberId (FindMemberIdDto findMemberIdDto) throws MessagingException;

    void findMemberPassword(FindPasswordDto findPasswordDto);


    MemberResponseDto getMemberInfo(String username);

    void deleteMember(String username);

    MemberInfoDto modifyMemberInfo(String username , ModifiedMemberInfoDto modifiedMemberInfoDto) throws IOException;

    void changePassword(String username,ChangePasswordDto changePasswordDto);

    Member findMemberByUsername(String username);

    Member findMemberByNickName(String nickname);

    ImageResponseDto modifyMemberImage(String username, MultipartFile modifyImage);

    NicknameResponseDto modifyMemberNickname(String currentUsername, String changeName);
}
