package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName
import kotlin.reflect.typeOf

data class MemberDto (
    @SerializedName("nickname") val nickname : String,
    @SerializedName("profileImg") val profileImg : String?
) {
    override fun equals(other: Any?): Boolean {
        return if(other is MemberDto)
            this.nickname == other.nickname
        else false
    }
}