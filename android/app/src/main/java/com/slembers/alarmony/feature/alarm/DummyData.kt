package com.slembers.alarmony.feature.alarm

data class Alarm (
    val alarm_id: Int,
    val title: String,
    val hour: Int,
    val minute: Int,
    val ampm: String,
    val alarm_date: List<String>
)
val alarm1 = Alarm(
    0,
    "장덕모임",
    8,
    45,
    "오후",
    listOf("월", "화", "수", "목", "금", "토", "일")
)
val alarm2 = Alarm(
    1,
    "미라클 모닝",
    6,
    15,
    "오전",
    listOf("화", "금")
)
val alarm3 = Alarm(
    2,
    "새벽 기도",
    4,
    10,
    "오전",
    listOf("월", "화", "수", "목", "금")
)
val alarm4 = Alarm(
    3,
    "주말 아침운동",
    8,
    5,
    "오전",
    listOf("토", "일")
)
val alarm5 = Alarm(
    4,
    "주말 아침운동",
    8,
    5,
    "오전",
    listOf("토", "일")
)
val alarm6 = Alarm(
    5,
    "주말 아침운동",
    8,
    5,
    "오전",
    listOf("토", "일")
)
val alarm7 = Alarm(
    6,
    "주말 아침운동",
    8,
    5,
    "오전",
    listOf("토", "일")
)
val aaa = listOf(alarm1, alarm2, alarm3, alarm4, alarm5, alarm6, alarm7)

data class Noti(
    val alert_id: Int,
    val profile_img: String,
    val content: String,
    val type: Boolean
)

val noti1 = Noti(
    0,
    "https://cdn-icons-png.flaticon.com/512/149/149071.png",
    "'미라클모닝' 그룹 초대입니다.",
    true
)

val noti2 = Noti(
    1,
    "https://ps.w.org/user-avatar-reloaded/assets/icon-256x256.png?rev=2540745",
    "'딘딘'님이 초대를 거절하셨습니다.",
    false
)
val notiSample = listOf(noti1, noti2)