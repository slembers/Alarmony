package com.slembers.alarmony.network.repository

import android.util.Log
import com.slembers.alarmony.model.db.LoginRequest
import com.slembers.alarmony.network.api.AlarmonyServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.concurrent.thread

object MemberService {

    fun login() {
        val memberApi = AlarmonyServer.memberApi
        try {
            Log.d("Start", "login --> 로그인 시도")
            memberApi.login(
                LoginRequest(
                    username = "test00001",
                    password = "1q2w3e4r!@"
                )
            ).enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    Log.i("response","response")
                    if(response.isSuccessful) {
                        Log.d("success", "로그인 성공하였습니다.")
                    } else {
                        Log.d("Failed", "로그인 통신하였습니다.")
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.i("disconnection", "로그인 실패하였습니다..")
                }
            })
        } catch ( e : Exception ) {
            println(e.message)
        }
        Log.d("Exit", "login <-- 로그인 종료")
    }
}