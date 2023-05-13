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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun saveAlarm(alarmDto: AlarmDto, context: Context) {
    lateinit var repository: AlarmRepository
    val alarmDao = AlarmDatabase.getInstance(context).alarmDao()
    repository = AlarmRepository(alarmDao)
    val alarm : Alarm = Alarm.toEntity(alarmDto)
    CoroutineScope(Dispatchers.IO).launch {
        repository.addAlarm(alarm)
    }
    setAlarm(alarmDto, context)
}
fun deleteAlarm(alarmId: Long, context: Context) {
    lateinit var repository: AlarmRepository
    CoroutineScope(Dispatchers.IO).launch {
        val alarmDao = AlarmDatabase.getInstance(context).alarmDao()
        repository = AlarmRepository(alarmDao)
        val alarm = repository.findAlarm(alarmId)
        if (alarm != null) {
            repository.deleteAlarm(alarm)
        }
    }
    cancelAlarm(alarmId, context)
}
fun cancelAlarm(alarmId: Long, context: Context) {
    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(context, alarmId.toInt(), intent, PendingIntent.FLAG_MUTABLE)
    alarmManager.cancel(pendingIntent)
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

@OptIn(ExperimentalMaterial3Api::class)
fun setAlarm(alarmDto: AlarmDto, context: Context) {
    val intervalDay : Long = 24*60*60*1000 // 24시간
    var newTime = calAlarm(alarmDto)
    var curTime = System.currentTimeMillis()

    if (curTime > newTime) {    // 설정한 시간이, 현재 시간 보다 작다면 바로 울리기 때문에 다음날로 설정
        newTime += intervalDay
    }
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("alarmId", alarmDto.alarmId)
    val myPendingIntent : Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    val alarmIntentRTC: PendingIntent =
        PendingIntent.getBroadcast(
            context,
            alarmDto.alarmId.toInt(),
            intent,
            myPendingIntent
        )
    val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
    val alarmInfo = AlarmManager.AlarmClockInfo(newTime, alarmIntentRTC)
    alarmManager.setAlarmClock(alarmInfo, alarmIntentRTC)

    val receiver = ComponentName(context, AlarmReceiver::class.java)
    context.packageManager.setComponentEnabledSetting(
        receiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
}

////////////////////// 테스트 코드입니다 ////////////////////

fun saveTestAlarm(alarmDto: AlarmDto, context: Context) {
    Log.d("save", alarmDto.toString())
    CoroutineScope(Dispatchers.IO).launch {
        lateinit var repository: AlarmRepository
        val alarmDao = AlarmDatabase.getInstance(context).alarmDao()
        repository = AlarmRepository(alarmDao)
        val alarm : Alarm = Alarm.toEntity(alarmDto)
        repository.addAlarm(alarm)
    }
    setTestAlarm(alarmDto, context)
}

fun setTestAlarm(alarmDto: AlarmDto, context: Context) {
    val newTime = System.currentTimeMillis() + (8 * 1000)  // 테스트용 코드 (8초 뒤 알람 설정)
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("alarmId", alarmDto.alarmId)
    }
    val myPendingIntent : Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }
    val alarmIntentRTC: PendingIntent =
        PendingIntent.getBroadcast(
            context,
            alarmDto.alarmId.toInt(),
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
fun goMain(context : Context) {
    val intent = Intent(context, MainActivity::class.java)
    context.startActivity(intent)
}
