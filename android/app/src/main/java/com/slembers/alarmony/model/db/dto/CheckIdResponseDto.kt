package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class CheckIdResponseDto(

    @SerializedName("isDulicated") val isDulicated: Boolean

)
