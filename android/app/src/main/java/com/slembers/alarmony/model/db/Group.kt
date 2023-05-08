package com.slembers.alarmony.model.db

import com.google.gson.annotations.SerializedName

data class Group(
    val title : String = "그룹생성",
    val hour : Int = 11,
    val minute : Int = 0,
    @SerializedName("alarm_Date") val alarmDate : List<Boolean>,
    val members : List<String>?,
    val soundName : String,
    val soundVolume : Float = 7f,
    val vibrate : Boolean = false
)