package com.slembers.alarmony.model.db.dto

import com.google.gson.annotations.SerializedName

data class MemberListDto (
    @SerializedName("memberInfoList") val memberList : List<MemberDto>
)