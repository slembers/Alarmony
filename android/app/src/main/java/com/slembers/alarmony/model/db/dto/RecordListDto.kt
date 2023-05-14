package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class RecordDto(
    @SerializedName("nickname") val nickname : String,
    @SerializedName("profileImg") val profileImg : String?,
    @SerializedName("success") val success : Boolean,
    @SerializedName("message") val message : String?
)

data class RecordListDto (
    @SerializedName("alarmList") val alarmList : List<RecordDto>?
)