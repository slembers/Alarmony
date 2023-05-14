package com.slembers.alarmony.network.service

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.slembers.alarmony.model.db.ReportRequestDto
import com.slembers.alarmony.model.db.dto.ReportDto
import com.slembers.alarmony.model.db.dto.ReportListDto
import com.slembers.alarmony.network.api.AlarmonyServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ReportService {
    val reportApi = AlarmonyServer().reportApi

    fun createReport(
        reportType: String?,
        reportedNickname: String?,
        content: String?,
        context: Context
    ) {
        reportApi.createReport(
            ReportRequestDto(
                reportType = reportType,
                reportedNickname = reportedNickname,
                content = content
            )
        ).enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit>,
                response: Response<Unit>
            ) {
                Log.i("response", "신고 생성 성공")
                Toast.makeText(context, "신고 내역이 전송되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.i("response", "신고 생성 실패 $t")
            }
        })
    }

    suspend fun getReportList(): ReportListDto? {
        return try {
            val result = withContext(Dispatchers.IO) {
                reportApi.getReportList().body()
            }
            result
        } catch (e: Exception) {
            Log.i("response", "신고 목록 가져오기 실패 $e")
            null
        }
    }

    suspend fun getReportDetail(reportId: Long): ReportDto? {
        return try {
            val result = withContext(Dispatchers.IO) {
                reportApi.getReportDetail(reportId).body()
            }
            result
        } catch (e: Exception) {
            Log.i("response", "신고 상세 가져오기 실패 $e")
            null
        }
    }
}