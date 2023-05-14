package com.slembers.alarmony.feature.notification

import com.google.gson.annotations.SerializedName
import com.slembers.alarmony.feature.alarm.Alarm

data class getAllNotisResponseDto(
    @SerializedName("alerts") val alerts : List<NotiDto>
)