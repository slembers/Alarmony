package com.slembers.alarmony.member.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity(name = "member")
@DynamicInsert
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @Column(name ="password", nullable = false)
    private String password;


    @Column(name ="phone_number", nullable = false , unique = true)
    private  String phoneNumber;

    @Column(name ="authority")
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ROLE_USER'")
    private AuthorityEnum authority;

    @Column(name = "profile_img_url", length = 1000)
    private String profileImgUrl;

}