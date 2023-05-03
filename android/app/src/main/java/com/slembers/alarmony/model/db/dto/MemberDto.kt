package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class MemberDto (
    @SerializedName("nickname") val nickname : String,
    @SerializedName("profileImg") val profileImg : String
)