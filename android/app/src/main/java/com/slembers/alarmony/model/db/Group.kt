package com.slembers.alarmony.model.db

data class Group(
    val title : String = "그룹생성",
    val hour : Int = 11,
    val minute : Int = 0,
    val alarmDate : List<Boolean>,
    val members : List<String>,
    val soundName : String,
    val soundVolume : Float = 7f,
    val vibrate : Boolean = false
)