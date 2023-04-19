package com.slembers.alarmony.member.repository;

import com.slembers.alarmony.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByUsername(String username);
}
