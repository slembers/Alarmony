package com.slembers.alarmony.model.db

import com.google.gson.annotations.SerializedName

data class FindPasswordRequest(

    @SerializedName("username") val username: String?,
    @SerializedName("email") val email: String?,


)
