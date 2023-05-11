package com.slembers.alarmony.feature.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notis")
class Noti(
    @PrimaryKey
    val notiId: Long,
    val profileImg: String,
    val content: String,
    val type: String
) {
    companion object {
        fun toEntity(notiDto: NotiDto): Noti {
            val noti = Noti(
                notiDto.notiId,
                notiDto.profileImg,
                notiDto.content,
                notiDto.type,
            )
            return noti
        }
    }
}

