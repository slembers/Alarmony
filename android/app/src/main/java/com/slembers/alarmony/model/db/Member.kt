package com.slembers.alarmony.model.db

data class Member (
    val profile : String? = null,
    val nickname : String
)

val memberList = listOf<Member>(
    Member(nickname = "홍길동"),
    Member(nickname = "이순신"),
    Member(nickname = "나랑드"),
    Member(nickname = "유비사"),
    Member(nickname = "하일루"),
    Member(nickname = "김두두"),
    Member(nickname = "나나나")
)