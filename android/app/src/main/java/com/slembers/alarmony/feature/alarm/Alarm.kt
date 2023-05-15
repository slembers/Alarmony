package com.slembers.alarmony.feature.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@TypeConverters(DayConverters::class)
@Entity(tableName = "alarms")
class Alarm(
    @PrimaryKey
    val alarmId: Long,
    val title: String,
    val hour: Int,
    val minute: Int,
    val alarmDate: List<Boolean>,
    val soundName: String,
    val soundVolume: Int,
    val vibrate: Boolean,
    val host: Boolean,
    val content: String
) {
    companion object {
        fun toEntity(alarmDto: AlarmDto): Alarm {
            val alarm = Alarm(
                alarmDto.alarmId,
                alarmDto.title,
                alarmDto.hour,
                alarmDto.minute,
                alarmDto.alarmDate,
                alarmDto.soundName,
                alarmDto.soundVolume,
                alarmDto.vibrate,
                alarmDto.host,
                alarmDto.content
            )
            return alarm
        }
    }
}
