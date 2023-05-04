package com.slembers.alarmony.network.repository

import com.slembers.alarmony.model.db.Group
import com.slembers.alarmony.model.db.dto.MemberListDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GroupRepositroy {

    @POST("alarms")
    fun addGroupAlarm(
        @Body group : Group
    ) : Call<Unit>

    @GET("groups/inviteable-members")
    fun searchGroup(
        @Query("groupId") groupId : String? = null,
        @Query("keyword") keyword : String
    ) : Call<MemberListDto>

}