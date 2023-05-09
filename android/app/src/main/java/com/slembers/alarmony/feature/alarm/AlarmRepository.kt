package com.slembers.alarmony.feature.alarm

import androidx.lifecycle.LiveData

class AlarmRepository(private val alarmDao: AlarmDao) {
    val readAllData : LiveData<List<Alarm>> = alarmDao.getAllAlarms()

    fun findAlarm(alarm_id : Long) : Alarm? {
        val alarm : Alarm? = alarmDao.getAlarmById(alarm_id)
        return alarm
    }

    suspend fun addAlarm(alarm: Alarm) {
        alarmDao.insertAlarm(alarm)
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.updateAlarm(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.deleteAlarm(alarm)
    }

    suspend fun deleteAllAlarms() {
        alarmDao.deleteAllAlarms()
    }
}