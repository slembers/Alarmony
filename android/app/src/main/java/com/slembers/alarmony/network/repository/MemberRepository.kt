package com.slembers.alarmony.network.repository

import com.slembers.alarmony.model.db.LoginRequest
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.model.db.dto.LoginResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MemberRepository {

    @GET("members")
    fun getMember(
        @Header("token") token : String?
    ) : Call<Member>

    @POST("members/login")
    fun login(
        @Body loginDto : LoginRequest
    ) : Call<LoginResponseDto>

}