package com.slembers.alarmony.model.db

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("username") val username : String?,
    @SerializedName("password") val password : String?
)

