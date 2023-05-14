package com.slembers.alarmony.network.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.graphics.convertTo
import androidx.navigation.NavHostController
import com.slembers.alarmony.model.db.Group
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.model.db.Record
import com.slembers.alarmony.model.db.dto.GroupDto
import com.slembers.alarmony.model.db.dto.MemberListDto
import com.slembers.alarmony.model.db.dto.MessageDto
import com.slembers.alarmony.model.db.dto.RecordListDto
import com.slembers.alarmony.network.api.AlarmonyServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.POST
import javax.annotation.meta.When
import kotlin.streams.toList

object GroupService {
    val groupApi = AlarmonyServer().groupApi
    suspend fun addGroupAlarm(
        title : String?,
        hour : Int,
        minute : Int,
        alarmDate : List<Boolean> = List<Boolean>(size = 7, init = { false }),
        members : List<String>?,
        soundName : String? = "자장가",
        soundVolume : Float? = 7f,
        vibrate : Boolean? = true
    ) : Long? {
        val groupApi = AlarmonyServer().groupApi
        try {
            val response = groupApi.addGroupAlarm(Group(
                title = title!!,
                hour = hour,
                minute = minute,
                alarmDate = alarmDate,
                soundName = soundName!!,
                soundVolume = soundVolume!!,
                vibrate = vibrate!!
            )).body()
            Log.d("response","[그룹생성] response : $response")
            val message = groupApi.addMembers(
                response?.groupId,
                hashMapOf("members" to (members ?: listOf()))
            )
            Log.d("response","[그룹생성] response : $message")

            return response?.groupId
        } catch ( e : Exception ) {
            Log.d("exception","저장 진생 중 에러 발행")
            Log.d("exception","에러 원인 : ${e.printStackTrace()}")
        }
        return -1
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

    suspend fun getGroupRecord(
        groupId : Long
    ) : Map<String,List<Record>> {
        val groupApi = AlarmonyServer().groupApi
        var result = hashMapOf<String, List<Record>>(
            "success" to listOf<Record>(),
            "failed" to listOf<Record>()
        )
        try {
            Log.d("getGroup","[알람 상세] 오늘의 알림 검색..")
            var successItems : MutableList<Record> = mutableListOf()
            var failItems : MutableList<Record> = mutableListOf()
            val recordList = groupApi.getGroupRecord(groupId).body()
            Log.d("getGroup","[알람 상세] 오늘의 알림 현황 : ${recordList}")
            recordList?.alarmList.let {
                it?.map {
                   val temp = Record(it.nickname,it.profileImg,it.success)
                    when(temp.success) {
                        true -> successItems.add(temp)
                        else -> failItems.add(temp)
                    }
                }
            }
            result.put("success",successItems)
            result.put("failed",failItems)
            return result
        } catch (e : Exception) {
            Log.d("getGroup","[알람 상세] 오늘의 알림 불러오기 오류 : ${e.message}")
            return result
        }
        return result
    }

    suspend fun deleteGroup(
        groupId : Long
    ) : Boolean {
        try {
            val response = groupApi.deleteGroup(groupId)
            Log.d("deleteGroup","[그룹 나가기] 나가기 결과 : $response")
            if(response.code() in 200..299) return true
        } catch ( e : Exception ) {
            Log.d("deleteGroup","[그룹 나가기] 그룹나가기 오류발생 : ${e.message}")
            return false
        }
        return false
    }

    suspend fun notification(
        groupId: Long,
        nickname: String
    ) : Boolean {
        try {
            val response = groupApi.notificationGroup(
                groupId,
                nickname
            )
            Log.d("notification","[그룹 알림] 알림 결과 : $response")
            when(response.code()) {
                in 200..299 -> return true
                in 400..499 -> return false
            }
        } catch ( e : Exception) {
            Log.d("notification","[그룹 알림] 알림 발송 중에 에러가 발생")
            Log.d("notification","[그룹 알림] 알림 발송 중에 에러 원인 : ${e.message}")
        }
        return false
    }

    suspend fun getGroupMemberList(
        groupId: Long
    ) : MutableList<Member> {
        try {
            val response = groupApi.getGroupRecord(groupId)
            val alarmList = response.body()
            Log.d("getGroup","[알람 상세 초대] 현재 초대 인원 현황 : ${alarmList}")
            if( alarmList?.alarmList?.isNotEmpty() == true ) {
                val list = alarmList.alarmList.let {
                    it.map {
                        Member(it.nickname,it.profileImg,it.success)

                    }.toMutableList()
                }
                return list
            }
        } catch( e : Exception) {
            Log.d("notification","[그룹 알림] 알림 발송 중에 에러가 발생")
            Log.d("notification","[그룹 알림] 알림 발송 중에 에러 원인 : ${e.message}")
        }
        return mutableListOf()
    }

}