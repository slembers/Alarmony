package com.slembers.alarmony.feature.alarm

import com.google.gson.annotations.SerializedName

data class getAllAlarmsResponseDto(
    @SerializedName("alarms") val alarms : List<AlarmDto>
)
