package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName


data class FindIdResponseDto (
    @SerializedName("message") val message: String
)