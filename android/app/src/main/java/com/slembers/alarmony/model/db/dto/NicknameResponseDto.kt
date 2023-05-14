package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class NicknameResponseDto (
    @SerializedName("success") val success : Boolean?,
    @SerializedName("nickname") val nickname : String?
)