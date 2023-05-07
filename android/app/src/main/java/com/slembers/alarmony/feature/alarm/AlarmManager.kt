package com.slembers.alarmony.feature.alarm

import android.content.Context
import android.icu.util.Calendar

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
}