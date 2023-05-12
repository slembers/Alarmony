package com.slembers.alarmony.feature.notification

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NotiService {
    @GET("alert")
    fun getAllNotis() : Call<List<NotiDto>>

    @POST("alert/{alert-id}/response")
    fun responseInvite(
        @Body accept : Boolean,
        @Path("alert-id") alertId : Long
    ) : Call<responseInviteDto>

    @DELETE("alert/{alert-id}")
    fun deleteNoti(
        @Path("alert_id") alertId : Long
    )
}