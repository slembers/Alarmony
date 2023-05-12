package com.slembers.alarmony.model.db

data class TokenReissueRequest(
    val grantType : String?,
    val username : String?,
    val refreshToken : String?
)
