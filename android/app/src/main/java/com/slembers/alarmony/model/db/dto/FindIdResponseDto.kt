package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName


data class FindIdResponseDto (
    @SerializedName("message") val message: String?,
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("error") val error: String?,
    @SerializedName("code") val code: String?,


)