package com.slembers.alarmony.feature.alarm

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AlarmDto(
    @SerializedName("alarm_id") val alarm_id : Long,
    @SerializedName("title") val title : String,
    @SerializedName("hour") val hour : Int,
    @SerializedName("minute") val minute : Int,
    @SerializedName("alarm_date") val alarm_date : List<Boolean>,
    @SerializedName("sound_name") val sound_name : String,
    @SerializedName("sound_volumn") val sound_volumn : Int,
    @SerializedName("vibrate") val vibrate : Boolean
) : Parcelable {
    companion object {
        fun toDto(alarm: Alarm): AlarmDto {
            val alarmDto = AlarmDto(
                alarm.alarm_id,
                alarm.title,
                alarm.hour,
                alarm.minute,
                alarm.alarm_date,
                alarm.sound_name,
                alarm.sound_volumn,
                alarm.vibrate
            )
            return alarmDto
        }
    }

}