package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class LogoutResponse(

    @SerializedName("token_type") val tokenType : String?,
)
