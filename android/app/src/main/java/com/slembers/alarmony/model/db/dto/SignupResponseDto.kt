package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class SignupResponseDto (

    @SerializedName("code") val code: Number,
    @SerializedName("message") val message: String,

)

