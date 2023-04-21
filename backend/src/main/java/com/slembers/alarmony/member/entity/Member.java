package com.slembers.alarmony.member.entity;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Entity(name = "member")
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_NOT_PERMITTED'")
    private AuthorityEnum authority;

    @Column(name = "profile_img_url", length = 1000)
    private String profileImgUrl;

    public void modifyAuthority(AuthorityEnum authority) {
        this.authority = authority;
    }

}
