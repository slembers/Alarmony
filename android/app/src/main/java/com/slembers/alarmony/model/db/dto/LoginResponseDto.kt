package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class LoginResponseDto (
    @SerializedName("token_type") val tokenType : String?,
    @SerializedName("access_token") val accessToken : String?,
    @SerializedName("refresh_token") val refreshToken : String?,
    @SerializedName("message") val message : String?,
    @SerializedName("status") val status : String?,

)