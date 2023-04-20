package com.slembers.alarmony.member.repository;

import com.slembers.alarmony.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    /**
     * 유저네임을 기준으로 Member를 찾는다.
     *
     * @param username
     * @return
     */
    Optional<Member> findByUsername(String username);

    /**
     * 닉네임을 기준으로 Member를 찾는다.
     *
     * @param nickname 닉네임
     * @return nickname으로 조회한 멤버
     */
    Optional<Member> findByNickname(String nickname);
}
