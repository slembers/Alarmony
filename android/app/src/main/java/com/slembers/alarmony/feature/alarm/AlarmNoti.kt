package com.slembers.alarmony.feature.alarm

import android.annotation.SuppressLint
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.core.app.NotificationCompat
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.slembers.alarmony.R


object AlarmNoti {
    private var NotificationID = 1005
    private var notification: Notification? = null
    private var notificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    private var ringtone: Ringtone? = null
    private var vibrator : Vibrator? = null

    @SuppressLint("ServiceCast")
    fun runNotification(context: Application, alarmDto: AlarmDto) {
        NotificationID = alarmDto.alarm_id.toInt()
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder = NotificationCompat.Builder(context.applicationContext, "notify_001")

        mBuilder!!.setSmallIcon(R.drawable.mas)
        mBuilder!!.setContentTitle(alarmDto.title)
        mBuilder!!.setAutoCancel(false)
        mBuilder!!.setOngoing(true)
        mBuilder!!.priority = Notification.PRIORITY_HIGH
        mBuilder!!.setOnlyAlertOnce(true)

        // 벨소리 가져오기
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, uri)
        val audioAttributes =
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()
        ringtone!!.volume = 1.0f
        ringtone!!.setAudioAttributes(audioAttributes)
        ringtone!!.play()

        // 사운드 ( 볼륨 범위 0.0f ~ 1.0f )
        var mp : MediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_ALARM_ALERT_URI)
//        val audioAttributes =
//            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()
//        mp.setAudioAttributes(audioAttributes)
//        mp.start();

        if (alarmDto.vibrate) { // 진동 체크한 경우 진동 설정
            val pattern = longArrayOf(0, 1000, 500, 1000, 500, 1000)
            vibrator =  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator;
            } else {
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator!!.vibrate(VibrationEffect.createWaveform(pattern, 0))
            } else {
                vibrator!!.vibrate(pattern, 0)
            }
        }

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
        if (vibrator != null) {
            vibrator!!.cancel()
        }
    }
}