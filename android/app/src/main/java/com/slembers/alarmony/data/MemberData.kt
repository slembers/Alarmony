package com.slembers.alarmony.data

data class MemberData (
    val profile : String? = null,
    val nickname : String
)

val memberList = listOf<MemberData>(
    MemberData(nickname = "홍길동"),
    MemberData(nickname = "이순신"),
    MemberData(nickname = "나랑드"),
    MemberData(nickname = "유비사"),
    MemberData(nickname = "하일루"),
    MemberData(nickname = "김두두"),
    MemberData(nickname = "나나나")
)