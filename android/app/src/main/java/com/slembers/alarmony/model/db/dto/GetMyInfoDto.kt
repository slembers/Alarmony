package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class GetMyInfoDto (
    @SerializedName("username") val username: String?,
    @SerializedName("nickname") val nickname: String?,
    @SerializedName("profileImgUrl") val profileImgUrl: String?,
    @SerializedName("email") val email: String?,
)