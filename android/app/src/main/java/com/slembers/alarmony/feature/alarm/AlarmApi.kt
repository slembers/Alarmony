package com.slembers.alarmony.feature.alarm

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.slembers.alarmony.feature.notification.NotiDto
import com.slembers.alarmony.feature.notification.saveNoti
import com.slembers.alarmony.network.api.AlarmonyServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AlarmApi {
    val alarmApi = AlarmonyServer().alarmApi

    fun getAllAlarmsApi(context : Context) {
        val call = alarmApi.getAllAlarms()
        var alarms : List<AlarmDto>? = null
        call.enqueue(object : Callback<getAllAlarmsResponseDto>{
            override fun onResponse(
                call: Call<getAllAlarmsResponseDto>,
                response: Response<getAllAlarmsResponseDto>
            ) {
                if (response.isSuccessful) {
                    val myResponse = response.body()
                    Log.d("myResponse", myResponse.toString())
                    if (myResponse!!.alarms != null) { // 서버에 알람 목록이 있으면
                        alarms = myResponse.alarms
                        for(alarm : AlarmDto in alarms!!) {    // Room 알람 목록 저장
                            saveAlarm(alarm, context)
                        }
                        Toast.makeText(
                            context,
                            "알람 불러오기 성공",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "알람이 없습니다",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("myResponse", "알람목록 응답 안옴")
                }
            }

            override fun onFailure(call: Call<getAllAlarmsResponseDto>, t: Throwable) {
                Log.e("myResponse", "네트워크 오류")
            }
        })
    }
}