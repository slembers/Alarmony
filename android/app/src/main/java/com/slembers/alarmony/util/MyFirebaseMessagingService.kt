package com.slembers.alarmony.util

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
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
        // 수신한 메시지를 처리
        val notificationManager = NotificationManagerCompat.from(applicationContext)

        val builder: NotificationCompat.Builder? =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
                    val channel =
                        NotificationChannel(
                            CHANNEL_ID,
                            CHANNEL_NAME,
                            NotificationManager.IMPORTANCE_DEFAULT
                        )
                    notificationManager.createNotificationChannel(channel)
                }
                NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            } else {
                NotificationCompat.Builder(applicationContext)
            }

        if (remoteMessage.data?.get("type").equals("ALARM")) {
            Log.i("디버깅", "알람 울려라!!!!!!!!!!!!!!!!!!!!!!!!!!");
            // 알람 울리는 로직 넣어주십쇼

            var group = remoteMessage.data?.get("group")

            builder?.setContentTitle("Alarmony")
                ?.setContentText("[$group] 알람이 울리는 중입니다.")
                ?.setSmallIcon(R.drawable.ic_launcher_background)

        } else {

            val title = remoteMessage.notification?.title
            val body = remoteMessage.notification?.body

            builder?.setContentTitle(title)
                ?.setContentText(body)
                ?.setSmallIcon(R.drawable.ic_launcher_background)
        }

        val notification: Notification = builder?.build()!!
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1, notification)
    }
}
