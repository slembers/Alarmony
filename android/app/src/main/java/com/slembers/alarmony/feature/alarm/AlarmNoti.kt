package com.slembers.alarmony.feature.alarm

import android.annotation.SuppressLint
import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.Settings
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.slembers.alarmony.R
import com.slembers.alarmony.util.soundConverter
import java.lang.Math.ceil
import java.lang.Math.round
import kotlin.math.roundToInt


object AlarmNoti {
    private var NotificationID = 1005
    private var notification: Notification? = null
    private var notificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    private var vibrator : Vibrator? = null
    private lateinit var audioManager : AudioManager
    private lateinit var mediaPlayer : MediaPlayer
    private var currentVolumn : Int = 0

    @SuppressLint("ServiceCast")
    fun runNotification(context: Application, alarmDto: AlarmDto) {
        NotificationID = alarmDto.alarmId.toInt()
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mBuilder = NotificationCompat.Builder(context.applicationContext, "notify_001")

        mBuilder!!.setSmallIcon(R.drawable.mas)
        mBuilder!!.setContentTitle(alarmDto.title)
        mBuilder!!.setAutoCancel(false)
        mBuilder!!.setOngoing(true)
        mBuilder!!.priority = Notification.PRIORITY_HIGH
        mBuilder!!.setOnlyAlertOnce(true)

        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        currentVolumn = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) // 현재 미디어 사운드 세팅
        mediaPlayer = MediaPlayer.create(context, soundConverter(context, alarmDto.soundName))
        mediaPlayer.isLooping = true
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        Log.d("VolumnMax", maxVolume.toString())
        Log.d("VolumnCurrent", currentVolumn.toString())
        val newVolume = alarmDto.soundVolume
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
        mediaPlayer.start()

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
        Log.d("myResponse-quit", "알람을 끕니다.")
        notificationManager!!.cancel(NotificationID)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolumn, 0) // 알람 울리기 전 사운드 세팅으로 돌려놓음.
        mediaPlayer.release()
        if (vibrator != null) {
            vibrator!!.cancel()
        }
    }
}