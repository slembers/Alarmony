package com.slembers.alarmony.network.service

import android.util.Log
import com.slembers.alarmony.model.db.Group
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.model.db.dto.MemberListDto
import com.slembers.alarmony.network.api.AlarmonyServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GroupService {

    fun addGroupAlarm(
        group: Group,
        connection: (Boolean) -> Unit
    ) {

        val groupApi = AlarmonyServer.groupApi
        groupApi.addGroupAlarm(
            group = group
        ).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                Log.i("response","response : ${response.body()}")
                if(response.isSuccessful) {
                    Log.d("success", "[그룹생성] 정상적으로 저장되었습니다.")
                    connection(true)
                } else {
                    Log.d("failed", "[그룹생성] 저장에 실패하였습니다.")
                    connection(false)
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.d("disconnection", "[그룹생성] 연결되지 않았습니다.")
            }
        })
    }

    fun searchMember(
        keyword : String,
        groupId : String? = null,
        memberList : (MemberListDto?) -> Unit
    ) {

        val group = AlarmonyServer.groupApi
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