package com.slembers.alarmony.feature.notification

import android.content.Context
import android.util.Log
import com.slembers.alarmony.feature.alarm.AlarmDto
import com.slembers.alarmony.feature.alarm.saveAlarm
import com.slembers.alarmony.network.api.AlarmonyServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NotiApi {
    val notiApi = AlarmonyServer().notiApi

    fun getAllNotis() : List<NotiDto>? {
        val allNotis = notiApi.getAllNotis()
        var notis : List<NotiDto>? = null
        allNotis.enqueue(object: Callback<List<NotiDto>> {
            override fun onResponse(call: Call<List<NotiDto>>, response: Response<List<NotiDto>>) {
                if (response.isSuccessful) {
                    notis = response.body()!!
                } else {
                }
            }
            override fun onFailure(call: Call<List<NotiDto>>, t: Throwable) {
                call.cancel()
                Log.e("API_ERROR", t.toString())
            }
        })
        return notis
    }

    fun responseInvite(accept : Boolean, alertId : Long, context : Context) {
        val responseInvite = notiApi.responseInvite(accept, alertId)
        Log.d("myResponse", "active")
        responseInvite.enqueue(object: Callback<responseInviteDto>{
            override fun onResponse(
                call: Call<responseInviteDto>,
                response: Response<responseInviteDto>
            ) {
                Log.d("myResponse1", response.toString())
                if (response.isSuccessful) {
                    val myResponse = response.body()
                    Log.d("myResponse", myResponse.toString())
                    val alarmDto = AlarmDto(
                        myResponse!!.alarm.alarmId,
                        myResponse.alarm.title,
                        myResponse.alarm.hour,
                        myResponse.alarm.minute,
                        myResponse.alarm.alarmDate,
                        myResponse.alarm.soundName,
                        myResponse.alarm.soundVolume,
                        myResponse.alarm.vibrate,
                        myResponse.alarm.host
                    )
                    saveAlarm(alarmDto, context)
                } else {
                    Log.e("myResponse", response.message())
                }
            }

            override fun onFailure(call: Call<responseInviteDto>, t: Throwable) {
                call.cancel()
                Log.e("myResponse", t.toString())
            }
        })
    }
}