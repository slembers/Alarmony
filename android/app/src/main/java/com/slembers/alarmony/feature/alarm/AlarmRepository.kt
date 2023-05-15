package com.slembers.alarmony.feature.alarm

import androidx.lifecycle.LiveData

class AlarmRepository(private val alarmDao: AlarmDao) {
    val readAllData : LiveData<List<Alarm>> = alarmDao.getAllAlarms()

    fun findAlarm(alarmId : Long) : Alarm? {
        val alarm : Alarm? = alarmDao.getAlarmById(alarmId)
        return alarm
    }

    fun getAllAlarms() : List<Alarm>? {
        val alarms = alarmDao.getAllAlarms2()
        return alarms
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