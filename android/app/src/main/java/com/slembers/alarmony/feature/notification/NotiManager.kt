package com.slembers.alarmony.feature.notification

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun saveNoti(notiDto: NotiDto, context: Context) {
    lateinit var repository: NotiRepository
    CoroutineScope(Dispatchers.IO).launch {
        val notiDao = NotiDatabase.getInstance(context).notiDao()
        repository = NotiRepository(notiDao)
        val noti : Noti = Noti.toEntity(notiDto)
        Log.d("myResponse", "${noti.content.toString()} : 알림이 저장 되었습니다. ")
        Log.d("myResponse", "${noti.notiId.toString()}")
        repository.addNoti(noti)
    }
}

fun deleteNoti(notiId: Long, context: Context) {
    lateinit var repository: NotiRepository
    CoroutineScope(Dispatchers.IO).launch {
        val notiDao = NotiDatabase.getInstance(context).notiDao()
        repository = NotiRepository(notiDao)
        val noti = repository.findNoti(notiId)
        if (noti != null) {
            repository.deleteNoti(noti)
        }
    }
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.cancel(notiId.toInt())
}

fun deleteAllNotis(context: Context) {
    lateinit var repository: NotiRepository
    CoroutineScope(Dispatchers.IO).launch {
        val notiDao = NotiDatabase.getInstance(context).notiDao()
        repository = NotiRepository(notiDao)
        repository.deleteAllNotis()
    }
}