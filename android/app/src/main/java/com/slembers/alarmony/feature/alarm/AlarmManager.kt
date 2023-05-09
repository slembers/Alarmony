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
import android.util.Log


fun saveAlarm(mAlarmViewModel: AlarmViewModel, alarmDto: AlarmDto, context: Context) {
    val alarm : Alarm = Alarm.toEntity(alarmDto)
    mAlarmViewModel.addAlarm(alarm)
    setAlarm(context, alarmDto)
}
fun calAlarm(alarmDto: AlarmDto) : Long {
    val calendar: Calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, alarmDto.hour)
    calendar.set(Calendar.MINUTE, alarmDto.minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    val alarmTime = calendar.timeInMillis
    return alarmTime
}
fun setAlarm(context: Context, alarmDto: AlarmDto) {
    val calendar: Calendar = Calendar.getInstance()
    val intervalDay : Long = 24*60*60*1000 // 24시간
    calendar.set(Calendar.HOUR_OF_DAY, alarmDto.hour)
    calendar.set(Calendar.MINUTE, alarmDto.minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    var newTime = calAlarm(alarmDto)
    var curTime = System.currentTimeMillis()

    if (curTime > newTime) {    // 설정한 시간이, 현재 시간 보다 작다면 바로 울리기 때문에 다음날로 설정
        newTime += intervalDay
    }
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("alarm", alarmDto)
    val myPendingIntent : Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    val alarmIntentRTC: PendingIntent =
        PendingIntent.getBroadcast(
            context,
            alarmDto.alarm_id.toInt(),
            intent,
            myPendingIntent
        )
    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, newTime, intervalDay, alarmIntentRTC)

    val receiver = ComponentName(context, AlarmReceiver::class.java)
    context.packageManager.setComponentEnabledSetting(
        receiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
}

fun deleteAlarm(mAlarmViewModel: AlarmViewModel, alarmDto: AlarmDto, context: Context) {
    val alarmDao = AlarmDatabase.getInstance(context).alarmDao()
    val repository = AlarmRepository(alarmDao)
    val alarm =  repository.findAlarm(alarmDto.alarm_id)
    if (alarm != null) {
        mAlarmViewModel.deleteAlarm(alarm)
        cancelAlarm(context, alarmDto)
    }
}

fun cancelAlarm(context : Context, alarmDto : AlarmDto) {
    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, alarmDto.alarm_id.toInt(), intent, PendingIntent.FLAG_MUTABLE)
    alarmManager.cancel(pendingIntent)
}

////////////////////// 테스트 코드입니다.

fun setAlarmTest(context: Context, alarmDto: AlarmDto) {
    val newTime = System.currentTimeMillis() + (8 * 1000)  // 테스트용 코드 (8초 뒤 알람 설정)
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("alarm", alarmDto)
    }
    Log.d("sendAlarm", alarmDto.toString())
    val myPendingIntent : Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    val alarmIntentRTC: PendingIntent =
        PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            myPendingIntent
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
