package com.slembers.alarmony.model.db

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    val username : String?,
    val password : String?
)