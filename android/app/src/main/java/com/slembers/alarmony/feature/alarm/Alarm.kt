package com.slembers.alarmony.feature.alarm

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@TypeConverters(DayConverters::class)
@Entity(tableName = "alarms")
class Alarm(
    @PrimaryKey
    val alarm_id: Long,
    val title: String,
    val hour: Int,
    val minute: Int,
    val alarm_date: List<Boolean>,
    val sound_name: String,
    val sound_volumn: Int,
    val vibrate: Boolean,
) {
    companion object {
        fun toEntity(alarmDto: AlarmDto): Alarm {
            val alarm = Alarm(
                alarmDto.alarm_id,
                alarmDto.title,
                alarmDto.hour,
                alarmDto.minute,
                alarmDto.alarm_date,
                alarmDto.sound_name,
                alarmDto.sound_volumn,
                alarmDto.vibrate
            )
            return alarm
        }
    }
}
