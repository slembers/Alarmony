package com.slembers.alarmony.member.repository;

import com.slembers.alarmony.member.dto.MemberInfoDto;
import com.slembers.alarmony.member.entity.Member;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     * @param username 유저네임
     * @return username으로 조회한 멤버
     */
    Optional<Member> findByUsername(String username);

    /**
     * 닉네임을 기준으로 Member를 찾는다.
     *
     * @param nickname 닉네임
     * @return nickname으로 조회한 멤버
     */
    Optional<Member> findByNickname(String nickname);


    /**
     * 이메일을 기준으로 Member를 찾는다.
     *
     * @param email 이메일 주소
     * @return 이메일 주소로 조회한 Member
     */
    Optional<Member> findMemberByEmail(String email);

    /**
     * 아이디와 이메일을 기준으로 Member를 찾는다.
     * @param username 회원 아이디
     * @param email 회원 이메일
     */
    Optional<Member> findMemberByUsernameAndEmail(String username, String email);

    /**
     * 그룹 id와 검색할 키워드로 초대 가능한 멤버 리스트를 찾는다. 그룹에 속한 멤버는 제외하고, 키워드가 들어간 멤버를 찾는다.
     *
     * @param groupId 그룹 id
     * @param keyword 검색할 키워드
     * @return 초대 가능한 멤버 목록
     */

    @Query("SELECT distinct new com.slembers.alarmony.member.dto.MemberInfoDto(m.nickname, m.profileImgUrl) "
        + "FROM member m "
        + "WHERE (m.nickname LIKE CONCAT('%', :keyword, '%') OR m.email LIKE CONCAT('%', :keyword, '%')) "
        + "AND m.id NOT IN ( "
        + "SELECT ma.member.id "
        + "FROM member_alarm ma "
        + "WHERE (ma.alarm.id IS NULL OR ma.alarm.id = :groupId)"
        + ") "
        + "ORDER BY "
        + "CASE WHEN m.nickname = :keyword THEN 0 "
        + "WHEN m.nickname LIKE CONCAT(:keyword, '%') THEN 1 "
        + "WHEN m.nickname LIKE CONCAT('%', :keyword, '%') THEN 2 "
        + "WHEN m.nickname LIKE CONCAT('%', :keyword) THEN 3 "
        + "ELSE 4 END")
    List<MemberInfoDto> findMembersWithGroupAndTeamByGroupId(@Param("groupId") Long groupId,
                                                             @Param("keyword") String keyword);

}
