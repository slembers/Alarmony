package com.slembers.alarmony.feature.notification

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun saveNoti(notiDto: NotiDto, context: Context) {
    lateinit var repository: NotiRepository
    val notiDao = NotiDatabase.getInstance(context).notiDao()
    repository = NotiRepository(notiDao)
    val noti : Noti = Noti.toEntity(notiDto)
    CoroutineScope(Dispatchers.IO).launch {
        repository.addNoti(noti)
    }
}

fun deleteNoti(notiId: Long, context: Context) {
    lateinit var repository: NotiRepository
    val notiDao = NotiDatabase.getInstance(context).notiDao()
    repository = NotiRepository(notiDao)
    val noti = repository.findNoti(notiId)
    CoroutineScope(Dispatchers.IO).launch {
        if (noti != null) {
            repository.deleteNoti(noti)
        }
    }
}

fun deleteAllNotis(context: Context) {
    lateinit var repository: NotiRepository
    val notiDao = NotiDatabase.getInstance(context).notiDao()
    repository = NotiRepository(notiDao)
    CoroutineScope(Dispatchers.IO).launch {
        repository.deleteAllNotis()
    }
}