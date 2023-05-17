package com.slembers.alarmony.model.db

import com.google.gson.annotations.SerializedName
data class Member(
    val nickname : String = "",
    val profileImg : String? = null,
    var isNew : Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        return if(other is Member) {
            this.nickname == (other as Member).nickname
        } else
            return false
    }

    override fun hashCode(): Int {
        return this.nickname.hashCode()
    }
}