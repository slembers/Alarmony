package com.slembers.alarmony.feature.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@TypeConverters(DayConverters::class)
@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey
    val alarm_id : Long,
    val title : String,
    val hour : Int,
    val minute : Int,
    val alarm_date : List<Boolean>,
    val sound_name : String,
    val sound_volumn : Int,
    val vibrate : Boolean
)