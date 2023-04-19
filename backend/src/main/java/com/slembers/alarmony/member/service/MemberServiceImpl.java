package com.slembers.alarmony.member.service;

import com.slembers.alarmony.member.dto.response.IdCheckResponseDto;
import com.slembers.alarmony.member.entity.Member;
import com.slembers.alarmony.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {


    private final MemberRepository memberRepo;

    /**
     * 아이디 중복체크
     **/

    @Override
    public IdCheckResponseDto checkForDuplicateId(String username) {

        return IdCheckResponseDto.builder().passed(memberRepo.existsByUsername(username)).build();
    }


    @Override
    public Member getMemberByUsername(String username) {
        return null;
    }

    @Override
    public Member getMemberByNickName(String nickname) {
        return null;
    }
}
