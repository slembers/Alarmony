package com.slembers.alarmony.feature.notification

import com.google.gson.annotations.SerializedName
import com.slembers.alarmony.feature.alarm.Alarm

data class InviteResponseDto(
    @SerializedName("message") val message : String,
    @SerializedName("alarm") val alarm : Alarm,
)
