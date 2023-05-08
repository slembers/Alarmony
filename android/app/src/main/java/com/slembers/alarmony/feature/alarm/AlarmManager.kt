package com.slembers.alarmony.feature.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build


fun saveAlarm(mAlarmViewModel: AlarmViewModel, alarm: Alarm, context: Context) {
    mAlarmViewModel.addAlarm(alarm)
    setAlarm(context, alarm)
}

fun setAlarm(context: Context, alarm: Alarm) {
    val calendar: Calendar = Calendar.getInstance()
    val intervalDay : Long = 24*60*60*1000 // 24시간
    calendar.set(Calendar.HOUR_OF_DAY, alarm.hour)
    calendar.set(Calendar.MINUTE, alarm.minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    var newTime = calendar.timeInMillis
    var curTime = System.currentTimeMillis()

    if (curTime > newTime) {    // 설정한 시간이, 현재 시간 보다 작다면 바로 울리기 때문에 다음날로 설정
        newTime += intervalDay
    }
//    val newTime = System.currentTimeMillis() + 5000  // 테스트용 코드 (5초 뒤 알람 설정)

    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("alarm", alarm)
    val alarmIntentRTC: PendingIntent =
        PendingIntent.getBroadcast(
            context,
            alarm.alarm_id.toInt(),
            intent,
            PendingIntent.FLAG_MUTABLE
        )
    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                newTime,
                alarmIntentRTC
            )

        }
        else -> {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                newTime,
                alarmIntentRTC
            )
        }
    }

    val receiver = ComponentName(context, AlarmReceiver::class.java)
    context.packageManager.setComponentEnabledSetting(
        receiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
}

fun deleteAlarm(mAlarmViewModel: AlarmViewModel, alarm: Alarm, context: Context) {
    mAlarmViewModel.deleteAlarm(alarm)
    cancelAlarm(context, alarm)
}

fun cancelAlarm(context : Context, alarm : Alarm) {
    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, alarm.alarm_id.toInt(), intent, PendingIntent.FLAG_MUTABLE)
    alarmManager.cancel(pendingIntent)
}
