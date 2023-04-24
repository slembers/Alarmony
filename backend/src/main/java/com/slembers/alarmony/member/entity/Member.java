package com.slembers.alarmony.member.entity;

import java.util.Objects;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return Objects.equals(this.getUsername(), member.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUsername());
    }

}
