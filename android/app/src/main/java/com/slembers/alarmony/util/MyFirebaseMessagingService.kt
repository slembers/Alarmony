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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.notification.NotiDto
import com.slembers.alarmony.feature.notification.saveNoti
import com.slembers.alarmony.network.repository.MemberService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

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
            Log.i("디버깅", "등록된 멤버가 없으니 등록 토큰을 저장합니다 $token");
            MainActivity.prefs.setString("registrationToken",token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.data?.get("type").equals("ALARM")) {
            Log.i("디버깅", "알람 울려라!!!!!!!!!!!!!!!!!!!!!!!!!!");
            // 알람 울리는 로직 넣어주십쇼

        } else {
            sendNotification(remoteMessage)

            val data = remoteMessage.data

            sendNotification(remoteMessage)
            val noti = NotiDto(
                data["alertId"]!!.toLong(),
                data["profileImg"]!!,
                data["content"]!!,
                data["type"]!!
            )
            saveNoti(noti, this)
        }
    }

    // 알림 생성 (아이콘, 알림 소리 등)
    private fun sendNotification(remoteMessage: RemoteMessage){
        // RemoteCode, ID를 고유값으로 지정하여 알림이 개별 표시 되도록 함
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("GO", "AlarmListActivity")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val myPendingIntent : Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_MUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(this, uniId, intent, myPendingIntent)

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
            if (remoteMessage.data["type"] == "INVITE") {
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
}
