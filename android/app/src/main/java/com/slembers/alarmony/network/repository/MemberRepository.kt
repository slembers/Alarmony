package com.slembers.alarmony.network.repository

import com.slembers.alarmony.model.db.LoginRequest
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.model.db.SignupRequest
import com.slembers.alarmony.model.db.dto.FindIdResponseDto
import com.slembers.alarmony.model.db.dto.LoginResponseDto
import com.slembers.alarmony.model.db.dto.SignupResponseDto
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

//    회원가입
    @POST("members/sign-up")
    fun signup(
        @Body signupDto : SignupRequest
    ) : Call<SignupResponseDto>



    @POST("members/find-id")
    fun findId(
    ) : Call<FindIdResponseDto>



}