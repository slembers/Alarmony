package com.slembers.alarmony.model.db

import com.google.gson.annotations.SerializedName

data class SignupRequest (
    val username : String?,
    val password : String?,
    val nickname : String?,
    val email : String?,
)
