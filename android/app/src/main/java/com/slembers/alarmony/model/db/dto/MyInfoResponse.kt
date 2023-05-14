package com.slembers.alarmony.model.db.dto

import retrofit2.http.Multipart
import java.io.File

data class MyInfoResponse(
    val username: String,
    val nickname : String,
    val profileImg: String,
    val email: String
)

