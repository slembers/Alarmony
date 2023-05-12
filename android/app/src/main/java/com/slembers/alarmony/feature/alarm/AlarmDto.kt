package com.slembers.alarmony.feature.alarm

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class AlarmDto(
    @SerializedName("alarm_id") val alarmId : Long,
    @SerializedName("title") val title : String,
    @SerializedName("hour") val hour : Int,
    @SerializedName("minute") val minute : Int,
    @SerializedName("alarm_date") val alarmDate : List<Boolean>,
    @SerializedName("sound_name") val soundName : String,
    @SerializedName("sound_volumn") val soundVolume : Int,
    @SerializedName("vibrate") val vibrate : Boolean,
    @SerializedName("host") val host : Boolean
) {
    companion object {
        fun toDto(alarm: Alarm): AlarmDto {
            val alarmDto = AlarmDto(
                alarm.alarmId,
                alarm.title,
                alarm.hour,
                alarm.minute,
                alarm.alarmDate,
                alarm.soundName,
                alarm.soundVolume,
                alarm.vibrate,
                alarm.host
            )
            return alarmDto
        }
    }
}