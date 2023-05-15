package com.slembers.alarmony.feature.notification

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.slembers.alarmony.feature.alarm.AlarmDto
import com.slembers.alarmony.feature.alarm.saveAlarm
import com.slembers.alarmony.network.api.AlarmonyServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NotiApi {
    val notiApi = AlarmonyServer().notiApi

    fun getAllNotisApi(context : Context) {
        val call = notiApi.getAllNotis()
        call.enqueue(object: Callback<getAllNotisResponseDto> {
            override fun onResponse(
                call: Call<getAllNotisResponseDto>,
                response: Response<getAllNotisResponseDto>
            ) {
                if (response.isSuccessful) {
                    val myResponse = response.body()
                    if (myResponse!!.alerts != null) { // 서버에 알림 목록이 있으면
                        val notis = myResponse.alerts
                        for(notiDto : NotiDto in notis!!) {    // Room 알림 목록 저장
                            if (notiDto.profileImg == null) {
                                notiDto.profileImg = ""
                            }
                            saveNoti(notiDto, context)
                        }
                    }
                } else {
                    Log.e("myResponse", "알림목록 응답 안옴")
                }
            }
            override fun onFailure(call: Call<getAllNotisResponseDto>, t: Throwable) {
                Log.e("myResponse", "네트워크 오류")
            }
        })
    }

    fun InviteResponseApi(accept : Boolean, alertId : Long, context : Context) {
        val call = notiApi.responseInvite(accept, alertId)
        Log.d("myResponse", "active")
        call.enqueue(object: Callback<InviteResponseDto>{
            override fun onResponse(
                call: Call<InviteResponseDto>,
                response: Response<InviteResponseDto>
            ) {
                if (response.isSuccessful) {
                    val myResponse = response.body()
                    Log.d("myResponse_Invite", myResponse.toString())
                    if (myResponse!!.alarm == null) { // 토큰이 유효하지 않으면 alarm에 null 들어옴
                        Toast.makeText(
                            context,
                            myResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val alarmDto = AlarmDto(
                            myResponse!!.alarm.alarmId,
                            myResponse.alarm.title,
                            myResponse.alarm.hour,
                            myResponse.alarm.minute,
                            myResponse.alarm.alarmDate,
                            myResponse.alarm.soundName,
                            myResponse.alarm.soundVolume,
                            myResponse.alarm.vibrate,
                            myResponse.alarm.host,
                            myResponse.alarm.content
                        )
                        saveAlarm(alarmDto, context)    // 초대 수락 후 알람객체 반환받아서 저장하기
                        Toast.makeText(
                            context,
                            "'${myResponse.alarm.title}'에 참여완료",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.e("myResponse", response.message())
                }
            }

            override fun onFailure(call: Call<InviteResponseDto>, t: Throwable) {
                call.cancel()
                Log.e("myResponse", t.toString())
                Toast.makeText(
                    context,
                    "참여실패",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("myResponse", "네트워크 오류")
            }
        })
    }

    fun deleteNotiApi(alertId : Long, context : Context) {
        val call = notiApi.deleteNoti(alertId)
        call.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.d("myResponse", response.toString())
                Log.d("myResponse", response.body().toString())
                Log.d("myResponse", response.code().toString())
                if (response.isSuccessful) {
                    deleteNoti(alertId, context)
                    // 서버에서 알림 삭제 성공시, Room 에서도 알림을 삭제함
                    Log.d("myResponse", "알림 삭제 성공.")
                } else {
                    Log.e("myResponse", "알림 삭제에 실패했습니다.")
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("myResponse", "네트워크 오류")
            }

        })
    }
}