package com.slembers.alarmony.network.repository

import com.slembers.alarmony.model.db.ChangeNicknameRequestDto
import com.slembers.alarmony.model.db.FindIdRequest
import com.slembers.alarmony.model.db.FindPasswordRequest
import com.slembers.alarmony.model.db.LoginRequest
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.model.db.ModifyMemberInfoDto
import com.slembers.alarmony.model.db.RegistTokenDto
import com.slembers.alarmony.model.db.SignupRequest
import com.slembers.alarmony.model.db.TokenReissueRequest
import com.slembers.alarmony.model.db.dto.CheckEmailResponseDto
import com.slembers.alarmony.model.db.dto.FindIdResponseDto
import com.slembers.alarmony.model.db.dto.FindPasswordResponseDto
import com.slembers.alarmony.model.db.dto.LoginResponseDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import com.slembers.alarmony.model.db.dto.CheckIdResponseDto
import com.slembers.alarmony.model.db.dto.CheckNicnameResponseDto
import com.slembers.alarmony.model.db.dto.GetMyInfoDto
import com.slembers.alarmony.model.db.dto.ImageResponseDto
import com.slembers.alarmony.model.db.dto.NicknameResponseDto
import com.slembers.alarmony.model.db.dto.TokenReissueResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Query
import retrofit2.http.PUT
import retrofit2.http.Part

interface MemberRepository {
    @PUT("members/regist-token")
    suspend fun putRegistToken(
        @Body registTokenDto: RegistTokenDto
    ): Response<Unit>

    @GET("members")
    fun getMember(
        @Header("token") token: String?
    ): Call<Member>

    @POST("members/login")
    suspend fun login(
        @Body loginDto: LoginRequest
    ): Response<LoginResponseDto>

    @POST("members/refresh")
    suspend fun refresh(
        @Body tokenReissueRequest: TokenReissueRequest
    ): Response<TokenReissueResponse>

    @POST("members/sign-up")
    fun signup(
        @Body signupDto: SignupRequest
    ): Call<Unit>


    @POST("members/find-id")
    fun findId(
        @Body findIdDto: FindIdRequest
    ): Call<FindIdResponseDto>


    @POST("members/find-pw")
    fun findPassword(
        @Body findPasswordDto: FindPasswordRequest
    ): Call<FindPasswordResponseDto>

    @GET("members/logout")
    suspend fun logOut(
    ) :Response<Unit>

    @GET("members/info")
    suspend fun getMyInfo(
    ): Response<GetMyInfoDto>


    @DELETE("member")
    fun signOut(
    ): Call<Unit>


    @GET("members/check-id")
    fun checkId(@Query("username") username: String): Call<CheckIdResponseDto>

    @GET("members/check-email")
    fun checkEmail(@Query("email") email: String): Call<CheckEmailResponseDto>

    @GET("members/check-nickname")
    fun checkNickname(@Query("nickname") nickname: String): Call<CheckNicnameResponseDto>

    @Multipart
    @PATCH("members")
    fun modifyMemberInfo(
        @Part modifiedMemberInfo : ModifyMemberInfoDto
    ) : Call<Unit>

    @Multipart
    @PATCH("members/image")
    suspend fun modifyMemberImage(
        @Part imgProfileFile : MultipartBody.Part
    ) : Response<ImageResponseDto>

    @POST("members/nickname")
    suspend fun modifyMemberNickname(
        @Body changeNicknameRequestDto : ChangeNicknameRequestDto
    ) : Response<NicknameResponseDto>
}