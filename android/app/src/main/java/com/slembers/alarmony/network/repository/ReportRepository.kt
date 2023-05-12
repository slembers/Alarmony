package com.slembers.alarmony.network.repository

import com.slembers.alarmony.model.db.ReportRequestDto
import com.slembers.alarmony.model.db.dto.ReportDto
import com.slembers.alarmony.model.db.dto.ReportListDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReportRepository {

    @POST("reports")
    fun createReport(
        @Body reportRequestDto: ReportRequestDto
    ) : Call<Unit>

    @GET("reports")
    suspend fun getReportList(
    ) : Response<ReportListDto>

    @GET("reports/{reportId}")
    suspend fun getReportDetail(
        @Path("reportId", encoded = true) reportId : Long?,
    ): Response<ReportDto>
}