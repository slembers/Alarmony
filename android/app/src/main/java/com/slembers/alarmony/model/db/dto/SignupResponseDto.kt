package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class SignupResponseDto (

    @SerializedName("code") val code: Number,
    @SerializedName("timestamp") val timestamp: String,
    @SerializedName("status") val status: Number,
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,

)

