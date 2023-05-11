package com.slembers.alarmony.feature.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import com.slembers.alarmony.R
import com.slembers.alarmony.util.Constants.OPEN_TYPE
import com.slembers.alarmony.util.Constants.REFRESH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AlarmForegroundService : Service() {
    private lateinit var repository: AlarmRepository
    override fun onBind(intent: Intent): IBinder? {
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) : Int {
        if (intent == null) {
            Log.e("NullPointException", "intent is null")
            return START_NOT_STICKY
        } else {
            val alarmDao = AlarmDatabase.getInstance(application).alarmDao()
            repository = AlarmRepository(alarmDao)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                startForeground(
                    intent.getStringExtra(
                        OPEN_TYPE
                    )!!
                ) else
                startForeground(
                    1,
                    Notification()
                )
            val alarmId = intent.getLongExtra("alarmId", -1L)
            CoroutineScope(Dispatchers.IO).launch {
                val alarm = repository.findAlarm(alarm_id = alarmId)
                val alarmDto = AlarmDto.toDto(alarm!!)
                val alarms = repository.readAllData
                CoroutineScope(Dispatchers.Main).launch {
                    if (intent?.getStringExtra(OPEN_TYPE) == REFRESH) {
                        refreshAlarms(alarms)
                    } else {
                        startAlarm(alarmDto!!)
                    }
                }
            }
            return START_STICKY
        }
    }
    private fun refreshAlarms(alarms : LiveData<List<Alarm>>) {
        CoroutineScope(Dispatchers.IO).launch {
            val alarmList = alarms.value ?: return@launch
            for (alarm in alarmList) {
                val alarmDto = AlarmDto.toDto(alarm)
                setAlarm(alarmDto, this@AlarmForegroundService)
            }
            delay(5000)
            stopForeground(true)
            stopSelf()
        }
    }

    private fun startAlarm(alarmDto: AlarmDto) {

        CoroutineScope(Dispatchers.Main).launch {
            val newIntent = Intent(applicationContext, AlarmActivity::class.java)
            newIntent.putExtra("alarmId", alarmDto.alarmId)
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(newIntent)
            delay(2000)
            stopForeground(true)
            stopSelf()
        }
    }
    private fun startForeground(type: String) {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setContentTitle("")
            .setContentText(type)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val chan = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}

