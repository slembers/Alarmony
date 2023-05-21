package com.slembers.alarmony.feature.notification

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.slembers.alarmony.feature.alarm.AlarmDto
import com.slembers.alarmony.feature.alarm.saveAlarm
import com.slembers.alarmony.model.db.CompareRegistTokenRequestDto
import com.slembers.alarmony.network.api.AlarmonyServer
import com.slembers.alarmony.network.repository.MemberService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
                            Log.d("myResponse-Noti", "${notiDto.content.toString()} : 알림 불러오기")
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

    fun InviteResponseApi(accept : Boolean, notiId : Long, context : Context) {
        val call = notiApi.responseInvite(accept, notiId)
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
                        lateinit var contentElse : String
                        if (myResponse.alarm.content == null) {
                            contentElse = "알람 상세설명이 없습니다."
                        } else {
                            contentElse = myResponse.alarm.content
                        }
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
                            contentElse
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

    fun deleteNotiApi(notiId : Long, context : Context) {
        val call = notiApi.deleteNoti(notiId)
        call.enqueue(object : Callback<Unit>{
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.d("myResponse", response.toString())
                Log.d("myResponse", response.body().toString())
                Log.d("myResponse", response.code().toString())
                if (response.isSuccessful) {
                    deleteNoti(notiId, context)
                    // 서버에서 알림 삭제 성공시, Room 에서도 알림을 삭제함
                    Log.d("myResponse-deleteNoti", "알림 삭제 성공.")
                }
                else if (response.code() == 404) {
                    deleteNoti(notiId, context)
                    // 서버에서 이미 삭제된 알림, Room 에서도 알림을 삭제함
                    Log.d("myResponse-deleteNoti(noServer)", "알림 삭제 성공.")
                }
                else {
                    Log.e("myResponse-deleteNoti", "알림 삭제에 실패했습니다.")
                }
            }
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("myResponse", "네트워크 오류")
            }

        })
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    fun sendAutoLogoutAndChangeToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("등록 토큰", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("등록 토큰",token)
            if (token.isNotBlank()) {
                CoroutineScope(Dispatchers.IO).async {
                    try {
                        val response = notiApi.sendAutoLogout(
                            CompareRegistTokenRequestDto(
                                registToken = token
                            )
                        )

                        if (response.isSuccessful) {
                            if (response.body()?.success == true) {
                                Log.d("autoLogoutSend", "로그아웃 요청 보냄.")
                                CoroutineScope(Dispatchers.IO).async {
                                    MemberService.putRegistTokenAfterSignIn()
                                }
                            } else {
                                Log.d("autoLogoutSend", "본인 로그인.")
                            }
                        } else {
                            Log.d("autoLogoutSend", "로그아웃 실패 요청 보냄.")
                            CoroutineScope(Dispatchers.IO).async {
                                MemberService.putRegistTokenAfterSignIn()
                            }
                        }

                    } catch (e : Exception) {
                        Log.e("autoLogoutSend", "오류 발생 : $e")
                    }
                }
            } else {
                Log.d("등록 토큰", "등록 토큰이 존재하지 않습니다.")
            }
        })

    }

}