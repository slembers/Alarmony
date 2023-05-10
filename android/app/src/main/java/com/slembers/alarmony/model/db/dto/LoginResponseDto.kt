package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto (
    @SerializedName("tokenType") val tokenType : String?,
    @SerializedName("accessToken") val accessToken : String?,
    @SerializedName("refreshToken") val refreshToken : String?,
    @SerializedName("message") val message : String?,
    @SerializedName("status") val status : String?,

)