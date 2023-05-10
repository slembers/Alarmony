package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("message") val message : String
)