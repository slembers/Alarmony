package com.slembers.alarmony.feature.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import com.slembers.alarmony.util.Constants.BOOT_COMPLETED
import com.slembers.alarmony.util.Constants.FIRE_ALARM
import com.slembers.alarmony.util.Constants.OPEN_TYPE
import com.slembers.alarmony.util.Constants.REFRESH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {
    lateinit var repository: AlarmRepository
    override fun onReceive(context: Context, intent: Intent) {
        val newIntent = Intent(context, AlarmForegroundService::class.java)
        if (intent.action == BOOT_COMPLETED) {
            newIntent.putExtra(OPEN_TYPE, REFRESH)
        } else {
            val alarmId = intent.getLongExtra("alarmId", -1L)
            if (alarmId == -1L) return
            CoroutineScope(Dispatchers.IO).launch {
                val alarmDao = AlarmDatabase.getInstance(context).alarmDao()
                val alarm = alarmDao.getAlarmById(alarmId)
                val alarmDto = AlarmDto.toDto(alarm!!)
                if (alarmDto == null) return@launch
                newIntent.putExtra(OPEN_TYPE, FIRE_ALARM)
                newIntent.putExtra("alarmId", alarmDto.alarmId)
                val isSnooze = intent.getBooleanExtra("isSnooze", false)
                if (!isSnooze) { // 스누즈가 아닐 경우
                    val calendar: Calendar = Calendar.getInstance()
                    var todayDayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 2    // 오늘 요일 구하기
                    if (todayDayOfWeek == -1) todayDayOfWeek = 6
                    if (alarmDto!!.alarmDate[todayDayOfWeek] == false) return@launch // 오늘이 울리는 요일이 아니면 리턴
                    setAlarm(alarmDto, context)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(newIntent)
                    } else {
                        context.startService(newIntent)
                    }
                }
            }
        }
    }
}