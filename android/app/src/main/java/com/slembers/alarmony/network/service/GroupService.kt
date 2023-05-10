package com.slembers.alarmony.network.service

import android.content.Context
import android.util.Log
import androidx.navigation.NavHostController
import com.slembers.alarmony.model.db.Group
import com.slembers.alarmony.model.db.dto.GroupDto
import com.slembers.alarmony.model.db.dto.MemberListDto
import com.slembers.alarmony.model.db.dto.MessageDto
import com.slembers.alarmony.network.api.AlarmonyServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GroupService {

    fun addGroupAlarm(
        title : String?,
        hour : Int,
        minute : Int,
        alarmDate : List<Boolean> = List<Boolean>(size = 7, init = { false }),
        members : List<String>?,
        soundName : String? = "자장가",
        soundVolume : Float? = 7f,
        vibrate : Boolean? = true,
        context: Context,
        navController: NavHostController
    ) {
        val groupApi = AlarmonyServer().groupApi
        CoroutineScope(Dispatchers.IO).async {
            val responseGroup = groupApi.addGroupAlarm(Group(
                title = title!!,
                hour = hour,
                minute = minute,
                alarmDate = alarmDate,
                soundName = soundName!!,
                soundVolume = soundVolume!!,
                vibrate = vibrate!!
            ))
            Log.d("response","[그룹생성] response : $responseGroup")
            val message = groupApi.addMembers(
                responseGroup.groupId,
                hashMapOf("members" to (members ?: listOf()))
            )
            Log.d("response","[그룹생성] response : $message")
        }
//        groupApi.addGroupAlarm(
//            group = Group(
//                title = title!!,
//                hour = hour,
//                minute = minute,
//                alarmDate = alarmDate,
//                soundName = soundName!!,
//                soundVolume = soundVolume!!,
//                vibrate = vibrate!!
//            )
//        ).enqueue(object: Callback<GroupDto> {
//            override fun onResponse(call: Call<GroupDto>, response: Response<GroupDto>) {
//                Log.d("response","[그룹생성] response : ${response.body()}")
//                Log.d("response","[그룹생성] response error : ${response.errorBody()}")
//                if(response.isSuccessful && response.body() != null) {
//                    Log.d("success", "[그룹생성] 정상적으로 저장되었습니다.")
//                    groupApi.addMembers(
//                        response.body()?.groupId,
//                        hashMapOf(
//                            "members" to (members ?: listOf<String>())
//                        )
//                    ).enqueue(object:Callback<MessageDto>{
//                        override fun onResponse(call: Call<MessageDto>, response: Response<MessageDto>) {
//                            TODO("성공로직")
//                            Log.d("response","[초대멤버] response : ${response.body()}")
//                        }
//
//                        override fun onFailure(call: Call<MessageDto>, t: Throwable) {
//                            TODO("실패로직")
//                            Log.d("response","[초대멤버] response : $t")
//                        }
//                    })
//                    TODO("Room에 저장 & AlarmListScreen으로 이동")
//                } else {
//                    Log.d("failed", "[그룹생성] 저장에 실패하였습니다.")
//                }
//            }
//
//            override fun onFailure(call: Call<GroupDto>, t: Throwable) {
//                Log.d("disconnection", "[그룹생성] 연결되지 않았습니다.")
//            }
//        })
    }
    fun searchMember(
        keyword : String,
        groupId : String? = null,
        memberList : (MemberListDto?) -> Unit
    ) {

        val group = AlarmonyServer().groupApi
        Log.i("response", "[그룹검색] --> $keyword")

        group.searchGroup(
            groupId = groupId,
            keyword = keyword
        ).enqueue(object:Callback<MemberListDto> {
            override fun onResponse(
                call: Call<MemberListDto>,
                response: Response<MemberListDto>
            ) {
                Log.i("response", "[그룹검색] ${response.code()}")
                if(response.isSuccessful && response.body() != null) {
                    Log.d("success", "[그룹검색] 검색 성공... : ${response.body()}")
                    memberList(response.body())
                }
            }

            override fun onFailure(call: Call<MemberListDto>, t: Throwable) {
                Log.d("disconnection", "[그룹검색] 연결되지 않았습니다. : ${t.message} : []")
            }
        })
    }
}