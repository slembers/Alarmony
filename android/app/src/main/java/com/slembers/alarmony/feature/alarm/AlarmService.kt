package com.slembers.alarmony.feature.alarm

import retrofit2.Call
import retrofit2.http.GET

interface AlarmService {

    @GET("alarms")
    fun getAllAlarms() : Call<getAllAlarmsResponseDto>
}