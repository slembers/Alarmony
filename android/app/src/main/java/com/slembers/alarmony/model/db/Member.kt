package com.slembers.alarmony.model.db

import com.google.gson.annotations.SerializedName
data class Member(
    val nickname : String,
    val profileImg : String?,
    val isNew : Boolean = true
)