package com.slembers.alarmony.member.service;

import com.slembers.alarmony.member.dto.request.SignUpRequestDto;
import com.slembers.alarmony.member.dto.response.CheckDuplicateDto;
import com.slembers.alarmony.member.dto.vo.MemberVerificationDto;
import com.slembers.alarmony.member.entity.AuthorityEnum;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final ModelMapper modelMapper;

    private final MemberRepository memberRepository;


    @Transactional
    @Override
    public MemberVerificationDto signUp(SignUpRequestDto signUpRequestDto) {


        //아이디 중복 체크
        if(checkForDuplicateId(signUpRequestDto.getUsername()).isDuplicated()) throw  new RuntimeException();
        //닉네임 중복 체크
        if(checkForDuplicateNickname(signUpRequestDto.getNickname()).isDuplicated()) throw  new RuntimeException();
        //이메일 중복 체크
        if(checkForDuplicateEmail(signUpRequestDto.getEmail()).isDuplicated()) throw  new RuntimeException();

        //dto ->entity
        Member member = modelMapper.map(signUpRequestDto,Member.class);

        memberRepository.save(member);

        return new MemberVerificationDto(member.getUsername(), member.getEmail());

    }



    /**
     * 아이디 중복체크
     * @param username 유저 아이디
     * @return 존재여부
     **/

    @Override
    public CheckDuplicateDto checkForDuplicateId(String username) {

        return CheckDuplicateDto.builder().isDuplicated(memberRepository.existsByUsername(username)).build();

    }

    /**
     * 이메일 중복 체크
     * @param email :이메일주소
     * @return 존재여부
     */
    @Override
    public CheckDuplicateDto checkForDuplicateEmail(String email) {
        return CheckDuplicateDto.builder().isDuplicated(memberRepository.existsByEmail(email)).build();
    }


    /**
     * 닉네임 중복 체크
     * @param nickname : 닉네임
     * @return 존재여부
     */
    @Override
    public CheckDuplicateDto checkForDuplicateNickname(String nickname) {
        return CheckDuplicateDto.builder().isDuplicated(memberRepository.existsByNickname(nickname)).build();
    }

    /**
     * 권한 수정
     * @param member : 회원 Entity
     * @param userRole : 권한명
     */
    @Transactional
    @Override
    public void modifyUserRole(Member member, AuthorityEnum userRole) {
        member.modifyAuthority(userRole);
        memberRepository.save(member);
    }

    @Override
    public Member getMemberByUsername(String username) {
        return null;
    }

    @Override
    public Member getMemberByNickname(String nickname) {
        return null;
    }
}
