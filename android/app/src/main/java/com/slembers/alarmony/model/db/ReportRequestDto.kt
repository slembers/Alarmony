package com.slembers.alarmony.model.db

data class ReportRequestDto (
    val reportType : String?,
    val reportedNickname : String?,
    val content : String?
)