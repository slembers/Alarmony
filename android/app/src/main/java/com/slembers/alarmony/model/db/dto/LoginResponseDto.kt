package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

class LoginResponseDto (
    @SerializedName("token_type") val tokenType : String?,
    @SerializedName("access_token") val AccessToken : String?,
    @SerializedName("refresh_token") val RefreshToken : String?
)