package com.slembers.alarmony.feature.alarm

import android.content.Context

fun saveAlarm(mAlarmViewModel: AlarmViewModel, alarm: Alarm) {
    mAlarmViewModel.addAlarm(alarm)
}