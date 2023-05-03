@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)

package com.slembers.alarmony.network.service

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.model.db.LoginRequest
import com.slembers.alarmony.model.db.dto.LoginResponseDto
import com.slembers.alarmony.network.api.AlarmonyServer
import com.slembers.alarmony.viewModel.LoginViewModel
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.jvm.Throws

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
object MemberService {

    @Throws(IOException::class)
    fun login() {
        val memberApi = AlarmonyServer.memberApi

        Log.d("Start", "login --> 로그인 시도")
        memberApi.login(
            LoginRequest(
                username = "test00001",
                password = "1q2w3e4r!@"
            )
        ).enqueue(object : Callback<LoginResponseDto> {
            override fun onResponse(call: Call<LoginResponseDto>, response: Response<LoginResponseDto>) {
                Log.i("response","response")
                if(response.isSuccessful) {
                    Log.d("success", "[로그인] 성공하였습니다.")
                    Log.d("success", "[로그인] 토큰 타입 : ${response.body()?.tokenType}.")
                    Log.d("AccessToken", "[로그인] 엑세스 토큰 : ${response.body()?.AccessToken}")
                    Log.d("RefreshToken", "[로그인] 리플래쉬 토큰 : ${response.body()?.RefreshToken}")
                    MainActivity.prefs.setString("accessToken", response.body()?.AccessToken!!)
                    MainActivity.prefs.setString("refreshToken", response.body()?.AccessToken!!)
                } else {
                    Log.d("Failed", "로그인 통신하였습니다.")
                }
            }

            override fun onFailure(call: Call<LoginResponseDto>, t: Throwable) {
                Log.i("disconnection", "로그인 실패하였습니다..")
            }
        })
        Log.d("Exit", "login <-- 로그인 종료")
    }
}