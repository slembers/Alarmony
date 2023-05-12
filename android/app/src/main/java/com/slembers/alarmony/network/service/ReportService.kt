package com.slembers.alarmony.network.service

import android.util.Log
import com.slembers.alarmony.model.db.ReportRequestDto
import com.slembers.alarmony.model.db.dto.MemberListDto
import com.slembers.alarmony.network.api.AlarmonyServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ReportService {
    val reportApi = AlarmonyServer().reportApi

    fun createReport(
        reportType : String?,
        reportedNickname : String?,
        content : String?
    ){
        reportApi.createReport(
            ReportRequestDto(
                reportType = reportType,
                reportedNickname = reportedNickname,
                content = content
            )
        ).enqueue(object: Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                Log.i("response", "신고 생성 성공")
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.i("response", "신고 생성 실패 $t")
            }
        })
    }
}