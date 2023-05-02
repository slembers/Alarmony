package com.slembers.alarmony.model.db

import com.google.gson.annotations.SerializedName

data class SignupRequest (
    @SerializedName("username") val username : String?,
    @SerializedName("password") val password : String?,
    @SerializedName("nickname") val nickname : String?,
    @SerializedName("email") val email : String?,
)
