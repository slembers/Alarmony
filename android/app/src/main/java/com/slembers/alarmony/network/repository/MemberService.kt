package com.slembers.alarmony.network.repository

import android.util.Log
import com.slembers.alarmony.model.db.LoginRequest
import com.slembers.alarmony.model.db.SignupRequest
import com.slembers.alarmony.model.db.dto.LoginResponseDto
import com.slembers.alarmony.model.db.dto.SignupResponseDto
import com.slembers.alarmony.network.api.AlarmonyServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MemberService {
    val memberApi = AlarmonyServer.memberApi

    fun singup(username: String, password: String, nickname: String, email: String) {
        try {
            Log.d("가입", "signup ==> 회원가입시도")
            memberApi.signup(
                SignupRequest(
                    username = username,
                    password = password,
                    nickname =nickname,
                    email = email
                )
            ).enqueue(object : Callback<SignupResponseDto> {
                override fun onResponse(call: Call<SignupResponseDto>, response: Response<SignupResponseDto>) {
                    Log.i("response","response")
                    if(response.isSuccessful) {
                        Log.d("success", "회원가입 성공")
                    } else {
                        Log.d("Failed", "회원가입 실패")
                    }
                }

                override fun onFailure(call: Call<SignupResponseDto>, t: Throwable) {
                    Log.d("disconnection", "회원가입 실패하였습니다..")
                }
            })
        } catch ( e : Exception ) {
            println(e.message)
        }
    }

    fun login(username: String, password:String) {

        try {
            Log.d("Start", "login --> 로그인 시도")
            memberApi.login(
                LoginRequest(
//                    username = "test00001",
//                    password = "1q2w3e4r!@"
                        username = username,
                        password = password
                )
            ).enqueue(object : Callback<LoginResponseDto> {
                override fun onResponse(call: Call<LoginResponseDto>, response: Response<LoginResponseDto>) {
                    Log.i("response","response")
                    Log.i("response", response.toString())
                    if(response.isSuccessful) {
                        Log.d("success", "로그인 성공하였습니다.")

                    }
                }

                override fun onFailure(call: Call<LoginResponseDto>, t: Throwable) {
                    Log.i("disconnection", "로그인 실패하였습니다..")
                }
            })
        } catch ( e : Exception ) {
            println(e.message)
        }
        Log.d("Exit", "login <-- 로그인 종료")
    }
}