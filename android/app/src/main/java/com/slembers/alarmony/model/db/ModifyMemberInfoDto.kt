package com.slembers.alarmony.model.db

import okhttp3.MultipartBody

data class ModifyMemberInfoDto (
    val nickname : String?,
    val imgProfileFile : MultipartBody.Part
)