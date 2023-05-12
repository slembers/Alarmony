package com.slembers.alarmony.network.repository

import com.slembers.alarmony.model.db.ReportRequestDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportRepository {

    @POST("reports")
    fun createReport(
        @Body reportRequestDto: ReportRequestDto
    ) : Call<Unit>
}