package com.slembers.alarmony.feature.alarm

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 만약 동일한 PrimaryKey 가 있을 경우 덮어쓰기
    suspend fun insertAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): LiveData<List<Alarm>>

    @Query("SELECT * FROM alarms WHERE alarm_id=:alarm_id")
    fun getAlarmById(alarm_id: Long): Alarm?

    @Update
    suspend fun updateAlarm(alarm: Alarm)

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Query("DELETE FROM alarms")
    suspend fun deleteAllAlarms()
}