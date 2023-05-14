package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class ReportDto (
    @SerializedName("reportId") val reportId : Int?,
    @SerializedName("reportType") val reportType : String?,
    @SerializedName("reporterNickname") val reporterNickname : String?,
    @SerializedName("reportedNickname") val reportedNickname : String?,
    @SerializedName("content") val content : String?,
)

data class ReportListDto (
    @SerializedName("reports") val reports : List<ReportDto>?
)