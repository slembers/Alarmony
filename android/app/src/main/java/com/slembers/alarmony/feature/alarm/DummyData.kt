package com.slembers.alarmony.feature.alarm

data class AlarmData (
    val alarm_id : Long,
    val title : String,
    val hour : Int,
    val minute : Int,
    val alarm_date : List<Boolean>,
    val sound_name : String,
    val sound_volumn : Int,
    val vibrate : Boolean
)
val alarm1 = Alarm(
    0,
    "장덕모임",
    8,
    45,
    listOf(true, true, true, false, false, true, true),
    "자장가",
    15,
    true
)

val alarm2 = Alarm(
    1,
    "수완모임",
    18,
    45,
    listOf(true, true, true, true, true, false, false),
    "자장가",
    15,
    true
)

val alarm3 = Alarm(
    2,
    "신창모임",
    21,
    45,
    listOf(false, false, false, false, false, true, true),
    "자장가",
    15,
    false
)

val alarm4 = Alarm(
    3,
    "미라클 모닝",
    6,
    18,
    listOf(true, true, true, true, true, true, true),
    "자장가",
    15,
    false
)

val aaa = listOf(alarm1, alarm2, alarm3, alarm4)

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