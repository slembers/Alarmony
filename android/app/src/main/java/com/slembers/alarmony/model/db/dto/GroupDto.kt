package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("groupId") val groupId : Long
)