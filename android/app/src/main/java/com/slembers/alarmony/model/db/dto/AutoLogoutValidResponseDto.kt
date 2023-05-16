package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class AutoLogoutValidResponseDto (
    @SerializedName("success") val success : Boolean
)