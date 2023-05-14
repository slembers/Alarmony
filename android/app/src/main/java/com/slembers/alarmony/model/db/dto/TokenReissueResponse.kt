package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class TokenReissueResponse (
    @SerializedName("tokenType") val tokenType : String?,
    @SerializedName("accessToken") val accessToken : String?,
    @SerializedName("refreshToken") val refreshToken : String?,
)