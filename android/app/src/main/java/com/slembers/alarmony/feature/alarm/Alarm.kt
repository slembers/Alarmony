package com.slembers.alarmony.feature.alarm

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
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
) : Parcelable