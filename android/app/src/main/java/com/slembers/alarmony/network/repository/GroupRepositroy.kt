package com.slembers.alarmony.network.repository

import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param
import com.slembers.alarmony.model.db.Group
import com.slembers.alarmony.model.db.dto.GroupDto
import com.slembers.alarmony.model.db.dto.MemberListDto
import com.slembers.alarmony.model.db.dto.MessageDto
import com.slembers.alarmony.model.db.dto.RecordListDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupRepositroy {

    @POST("alarms")
    suspend fun addGroupAlarm(
        @Body group : Group
    ) : Response<GroupDto>

    @GET("groups/inviteable-members")
    fun searchGroup(
        @Query("groupId") groupId : String? = null,
        @Query("keyword") keyword : String
    ) : Call<MemberListDto>

    @POST("groups/{groupId}/members")
    suspend fun addMembers(
        @Path("groupId", encoded = true) groupId : Long?,
        @Body members : HashMap<String, List<String>>
    ) : Response<String>

    @GET("groups/{groupId}/records")
    suspend fun getGroupRecord(
        @Path("groupId") groupId : Long
    ) : Response<RecordListDto>

    @DELETE("groups/{groupId}")
    suspend fun deleteGroup(
        @Path("groupId") groupId : Long
    ) : Response<Unit>
}