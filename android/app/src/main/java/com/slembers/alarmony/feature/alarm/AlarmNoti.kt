package com.slembers.alarmony.feature.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.slembers.alarmony.R

object AlarmNoti {
    private var NotificationID = 1005
    private var notification: Notification? = null
    private var notificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    private var ringtone: Ringtone? = null

    fun runNotification(context: Context, alarm: Alarm) {
        NotificationID = alarm.alarm_id.toInt()
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder = NotificationCompat.Builder(context.applicationContext, "notify_001")

        mBuilder!!.setSmallIcon(R.mipmap.ic_launcher)
        mBuilder!!.setContentTitle(alarm.title)
        mBuilder!!.setAutoCancel(true)
        mBuilder!!.setOngoing(false)
        mBuilder!!.priority = Notification.PRIORITY_HIGH
        mBuilder!!.setOnlyAlertOnce(true)

        // 사운드
        val alarmBell: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, alarmBell)
        ringtone!!.play()

//        var mp : MediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
//        mp.start();

        // 진동
//        val vibrator =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
//            vibratorManager.defaultVibrator;
//        } else {
//            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        }

        mBuilder!!.build().flags = Notification.FLAG_NO_CLEAR or Notification.PRIORITY_HIGH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "channel_id"
            val channel =
                NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(500, 1000, 500, 1000)
            notificationManager!!.createNotificationChannel(channel)
            mBuilder!!.setChannelId(channelId)
        }
        notification = mBuilder!!.build()
        notificationManager!!.notify(NotificationID, notification)
    }

    fun cancelNotification() {
        notificationManager!!.cancel(NotificationID)
        ringtone!!.stop()
    }
}