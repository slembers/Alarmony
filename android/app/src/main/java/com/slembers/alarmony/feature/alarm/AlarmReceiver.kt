package com.slembers.alarmony.feature.alarm

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.slembers.alarmony.util.Constants.BOOT_COMPLETED
import com.slembers.alarmony.util.Constants.OPEN_TYPE
import com.slembers.alarmony.util.Constants.REFRESH
import com.slembers.alarmony.util.Constants.alarm_id

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

    }

}