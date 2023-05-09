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
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.alarm.AlarmDto
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
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int) : Int {
        val alarmDao = AlarmDatabase.getInstance(application).alarmDao()
        repository = AlarmRepository(alarmDao)
        val alarms = repository.readAllData
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForeground(
                intent!!.getStringExtra(
                    OPEN_TYPE
                )!!
            ) else
            startForeground(
                1,
                Notification()
            )
        intent.setExtrasClassLoader(AlarmDto::class.java.classLoader)
        val alarmDto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("alarm", AlarmDto::class.java) as AlarmDto
        } else {
            intent.getParcelableExtra<AlarmDto>("alarm") as AlarmDto
        }

        if (intent!!.getStringExtra(OPEN_TYPE) == REFRESH) {
            refreshAlarms(alarms)
        } else {
            startAlarm(alarmDto!!)
        }
        return START_STICKY
    }
    private fun refreshAlarms(alarms : LiveData<List<Alarm>>) { // 재부팅 시 알람 재세팅
        CoroutineScope(Dispatchers.IO).launch {
            CoroutineScope(Dispatchers.Main).launch {
                for (alarm in alarms as List<Alarm>) {
                    val alarmDto = AlarmDto.toDto(alarm)
                    setAlarm(this@AlarmForegroundService, alarmDto)
                }
            }
            delay(5000)
            stopForeground(true)
            stopSelf()
        }
    }

    private fun startAlarm(alarmDto: AlarmDto) {

        CoroutineScope(Dispatchers.Main).launch {

            val newIntent = Intent(applicationContext, AlarmActivity::class.java)
            newIntent.putExtra("alarm", alarmDto)
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

