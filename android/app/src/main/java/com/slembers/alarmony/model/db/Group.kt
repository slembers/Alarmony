package com.slembers.alarmony.model.db

import com.google.gson.annotations.SerializedName

data class Group(
    val title : String = "그룹생성",
    val hour : Int = 10,
    val minute : Int = 0,
    val alarmDate : List<Boolean>,
    val soundName : String,
    val soundVolume : Float = 7f,
    val vibrate : Boolean = false,
    val content : String = ""
)