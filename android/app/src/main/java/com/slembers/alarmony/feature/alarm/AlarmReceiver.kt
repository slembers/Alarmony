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

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val newIntent = Intent(context, AlarmForegroundService::class.java)

        if (intent.action == BOOT_COMPLETED) {
            newIntent.putExtra(OPEN_TYPE, REFRESH)
        } else {
            intent.setExtrasClassLoader(AlarmDto::class.java.classLoader) // 클래스 로더 설정
            val alarmDto : AlarmDto? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("alarm", AlarmDto::class.java) as AlarmDto
            } else {
                intent.getParcelableExtra<AlarmDto>("alarm")
            }
            Log.d("Alarm", alarmDto.toString())
            if (alarmDto == null) return
            newIntent.putExtra(OPEN_TYPE, FIRE_ALARM)
            newIntent.putExtra("alarm", alarmDto)
            val isSnooze = intent.getBooleanExtra("isSnooze", false)
            if (!isSnooze) { // 스누즈가 아닐 경우
                val calendar: Calendar = Calendar.getInstance()
                var todayDayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 2    // 오늘 요일 구하기
                if (todayDayOfWeek == -1) todayDayOfWeek = 6
                if (alarmDto!!.alarm_date[todayDayOfWeek] == false) return // 오늘이 울리는 요일이 아니면 리턴
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(newIntent)
            } else {
                context.startService(newIntent)
            }
        }
    }

}