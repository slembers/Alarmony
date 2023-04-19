package com.slembers.alarmony.member.service;

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
    public boolean checkForDuplicateId(String id) {

        return false;
    }


    @Override
    public Long getMemberByUsername(String username) {
        return null;
    }

    @Override
    public Long getMemberByNickName(String nickname) {
        return null;
    }
}
