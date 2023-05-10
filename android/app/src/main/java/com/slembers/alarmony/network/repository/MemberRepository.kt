package com.slembers.alarmony.network.repository

import com.slembers.alarmony.model.db.FindIdRequest
import com.slembers.alarmony.model.db.FindPasswordRequest
import com.slembers.alarmony.model.db.LoginRequest
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.model.db.SignupRequest
import com.slembers.alarmony.model.db.dto.CheckEmailResponseDto
import com.slembers.alarmony.model.db.dto.FindIdResponseDto
import com.slembers.alarmony.model.db.dto.FindPasswordResponseDto
import com.slembers.alarmony.model.db.dto.LoginResponseDto
import com.slembers.alarmony.model.db.dto.SignupResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import com.slembers.alarmony.model.db.dto.CheckIdResponseDto
import com.slembers.alarmony.model.db.dto.CheckNicnameResponseDto
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Body findIdDto : FindIdRequest
    ) : Call<FindIdResponseDto>


    @POST("members/find-pw")
    fun findPassword(
        @Body findPasswordDto : FindPasswordRequest
    ) : Call<FindPasswordResponseDto>

    @GET("auth/logout")
    fun logOut(


    ) :Call<Unit>

    @DELETE("member")
    fun signOut(
    ): Call<Unit>

    @GET("members/check-id")
    fun checkId(@Query("username") username: String): Call<CheckIdResponseDto>

    @GET("members/check-email")
    fun checkEmail(@Query("email") email: String): Call<CheckEmailResponseDto>

    @GET("members/check-nickname")
    fun checkNickname(@Query("nickname") nickname: String): Call<CheckNicnameResponseDto>

}