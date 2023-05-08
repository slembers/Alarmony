package com.slembers.alarmony.feature.alarm

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.icu.util.Calendar
import android.os.Build
import androidx.lifecycle.viewmodel.compose.viewModel
import com.slembers.alarmony.util.Constants.BOOT_COMPLETED
import com.slembers.alarmony.util.Constants.FIRE_ALARM
import com.slembers.alarmony.util.Constants.OPEN_TYPE
import com.slembers.alarmony.util.Constants.REFRESH
import com.slembers.alarmony.util.Constants.alarm_id

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val newIntent = Intent(context, AlarmForegroundService::class.java)

        if (intent.action == BOOT_COMPLETED) {
            newIntent.putExtra(OPEN_TYPE, REFRESH)
        } else {
            val alarm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra("alarm", Alarm::class.java)
            } else {
                intent.getParcelableExtra<Alarm>("alarm")
            }
            newIntent.putExtra(OPEN_TYPE, FIRE_ALARM)
            newIntent.putExtra("alarm", alarm)
            val calendar: Calendar = Calendar.getInstance()
            var todayDayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK) - 2    // 오늘 요일 구하기
            if (todayDayOfWeek == -1) todayDayOfWeek = 6
            if (alarm!!.alarm_date[todayDayOfWeek] == false) return // 오늘이 울리는 요일이 아니면 리턴

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                context.startForegroundService(newIntent)
            } else {
                context.startService(newIntent)
            }
        }
    }

}