package com.slembers.alarmony.feature.alarm

import com.slembers.alarmony.feature.notification.InviteResponseDto
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.time.LocalDateTime

interface AlarmService {

    @GET("alarms")
    fun getAllAlarms() : Call<getAllAlarmsResponseDto>

    @PUT("alarms/{alarm-id}/record")
    fun recordAlarm(
        @Body datetime: String,
        @Path("alarm-id") alarmId : Long
    ) : Call<Unit>

    @PUT("alarms/{alarm-id}/message")
    fun snoozeMessage(
        @Body message: String,
        @Path("alarm-id") alarmId : Long
    ) : Call<Unit>
}