package com.slembers.alarmony.model.db

import retrofit2.http.Multipart

data class ChangeMyInfoRequest(
    val nickname: String,
    val profileImage: Multipart
)
