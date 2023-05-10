package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class CheckNicnameResponseDto(
    @SerializedName("duplicated") val duplicated: Boolean
)
