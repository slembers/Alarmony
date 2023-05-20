package com.slembers.alarmony.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.NotificationCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.alarm.AlarmDatabase
import com.slembers.alarmony.feature.alarm.AlarmDto
import com.slembers.alarmony.feature.alarm.deleteAllAlarms
import com.slembers.alarmony.feature.alarm.deleteAlarm
import com.slembers.alarmony.feature.notification.NotiDto
import com.slembers.alarmony.feature.notification.deleteAllNotis
import com.slembers.alarmony.feature.sendAlarm.SendAlarmForegroundService
import com.slembers.alarmony.feature.notification.saveNoti
import com.slembers.alarmony.network.repository.MemberService
import com.slembers.alarmony.util.Constants.FIRE_ALARM
import com.slembers.alarmony.util.Constants.OPEN_TYPE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class MyFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val CHANNEL_ID = "1"
        private const val CHANNEL_NAME = "Alarmony"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("디버깅", "Refreshed token = $token");
        // TODO: 서버로 토큰 전송
        if(MainActivity.prefs.getString("accessToken","").isNotBlank()){
            CoroutineScope(Dispatchers.IO).async {
                MemberService.putRegistToken(token)
            }
        } else {
            Log.i("디버깅", "등록된 멤버가 없습니다. :  $token");
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        MainActivity.prefs = PresharedUtil(application)
        Log.d("파이어베이스","메시지 도착함")
        Log.d("remoteMessage", remoteMessage.toString())
        Log.d("remoteMessage", remoteMessage.data.toString())

        if (MainActivity.prefs == null || MainActivity.prefs.getString("accessToken","").isBlank()) {
            Log.d("LOGOUTED","로그아웃 상태라 FCM 요청이 거절되었습니다.")
            return
        }

        if (remoteMessage.data["type"].equals("AUTO_LOGOUT")) {
            if(!remoteMessage.data["receiver"].equals(MainActivity.prefs.getString("nickname",""))){
                Log.d("자동로그아웃", "로그아웃 요청이 왔지만 로그인 유저가 달라 거절되었습니다.");
                return
            }
            Log.d("자동로그아웃", "분기 입장");
            remoteMessage.data["content"] = "다른 기기에서 로그인하여 로그아웃 처리 되었습니다."
            sendDeletedNotification(remoteMessage)
            CoroutineScope(Dispatchers.IO).launch {
                val context = this@MyFirebaseMessagingService
                Log.d("자동로그아웃", "자동 로그아웃되야 함");
                showToast(context,"다른 기기에서 접속하여 로그아웃 처리됩니다.")
                deleteAllAlarms(context)
                deleteAllNotis(context)
                val result = MemberService.logOut()
                if (result) {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    applicationContext.startActivity(intent)
                }
            }
            return
        }

        if (!remoteMessage.data["receiver"].equals(MainActivity.prefs.getString("nickname",""))){
            Log.d("Duplicate Tokens","토큰이 같지만 다른 유저입니다.")
            return
        }

        // 알람 배달
        if (remoteMessage.data["type"].equals("ALARM")) {
            Log.i("디버깅", "알람 울려라!!!!!!!!!!!!!!!!!!!!!!!!!!");
            val newIntent = Intent(this, SendAlarmForegroundService::class.java)
            val alarmId = remoteMessage.data["alarmId"]!!.toLong()
            CoroutineScope(Dispatchers.IO).launch {
                val alarmDao = AlarmDatabase.getInstance(this@MyFirebaseMessagingService).alarmDao()
                val alarm = alarmDao.getAlarmById(alarmId)
                val alarmDto = AlarmDto.toDto(alarm!!)
                if (alarmDto == null) return@launch
                newIntent.putExtra("alarmId", alarmId)
                newIntent.putExtra(OPEN_TYPE, FIRE_ALARM)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    this@MyFirebaseMessagingService.startForegroundService(newIntent)
                } else {
                    this@MyFirebaseMessagingService.startService(newIntent)
                }
            }


        }
        else if (remoteMessage.data["type"].equals("DELETE") ||
                        remoteMessage.data["type"].equals("BANN")) {
            Log.d("myResponse", remoteMessage.toString())
            Log.d("myResponse", remoteMessage.data.toString())
            if (remoteMessage.data != null) {
                val data = remoteMessage.data
                sendNotification(remoteMessage)
                val alarmId = data["alarmId"]!!.toLong()
                deleteAlarm(alarmId, this)
            }
        }

        else if (remoteMessage.data["type"].equals("CHANGE_HOST")) {
            Log.d("HostChange", remoteMessage.toString())
            Log.d("HostChange", remoteMessage.data.toString())
            val data = remoteMessage.data
            sendNotification(remoteMessage)
            val alarmId = data["alarmId"]!!.toLong()
            Log.d("HostChange", "변경될 호스트 알람 아이디는 $alarmId")
        }

        else if (remoteMessage.data["type"].equals("MODIFY_ALARM")) {
            Log.d("ModifyAlarm", remoteMessage.toString())
            Log.d("ModifyAlarm", remoteMessage.data.toString())
            val data = remoteMessage.data
            sendNotification(remoteMessage)
            val alarmId = data["alarmId"]!!.toLong()
            Log.d("ModifyAlarm", "정보가 변경될 알람 아이디는 $alarmId")
        }

        // 나머지 알림
        else {
            Log.d("myResponse", remoteMessage.data.toString())
            if (remoteMessage.data != null) {
                val data = remoteMessage.data
                sendNotification(remoteMessage)
                Log.d("myResponse", "알림을 전달 받았습니다.")
            } else {
                Log.d("myResponse", "알림을 받을 수 없는 상태입니다.")
            }
        }
    }

    // 알림 생성 (아이콘, 알림 소리 등)
    private fun sendNotification(remoteMessage: RemoteMessage){
        val data = remoteMessage.data
        val type = data["type"]
        val alertId = data["alertId"]
        val profileImg = data["profileImg"]
        val content = data["content"]

        // 알림 저장
        val noti = NotiDto(
            alertId!!.toLong(),
            profileImg!!,
            content!!,
            type!!
        )
        saveNoti(noti, this)

        // RemoteCode, ID를 고유값으로 지정하여 알림이 개별 표시 되도록 함
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("GO", "Noti")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_MUTABLE)

        // 알림 채널 이름
        val channelId = "AlarmonyNotification"
        // 알림 소리
        val notiUrl = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.mas)     // 아이콘 설정
            .setContentTitle("alarmony")     // 제목
            .setContentText(remoteMessage.data["content"].toString())     // 메시지 내용
            .setAutoCancel(true)
            .setSound(notiUrl)     // 알림 소리
            .setContentIntent(pendingIntent)       // 알림 실행 시 Intent

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notiImfortance =
            if (type == "DELETE") {
                NotificationManager.IMPORTANCE_HIGH
            }
            else {
                NotificationManager.IMPORTANCE_DEFAULT
            }
        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Notice", notiImfortance)
            notificationManager.createNotificationChannel(channel)
        }
        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())
    }
    private fun sendDeletedNotification(remoteMessage: RemoteMessage){
        // RemoteCode, ID를 고유값으로 지정하여 알림이 개별 표시 되도록 함
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("GO", "Noti")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_MUTABLE)

        // 알림 채널 이름
        val channelId = "AlarmonyNotification"
        // 알림 소리
        val notiUrl = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보와 작업을 지정
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.mas)     // 아이콘 설정
            .setContentTitle("alarmony")     // 제목
            .setContentText(remoteMessage.data["content"].toString())     // 메시지 내용
            .setAutoCancel(true)
            .setSound(notiUrl)     // 알림 소리
            .setContentIntent(pendingIntent)       // 알림 실행 시 Intent

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notiImfortance = NotificationManager.IMPORTANCE_HIGH
        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, "Notice", notiImfortance)
            notificationManager.createNotificationChannel(channel)
        }
        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())
    }
}
