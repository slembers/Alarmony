package com.slembers.alarmony.feature.notification

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun saveNoti(notiDto: NotiDto, context: Context) {
    lateinit var repository: NotiRepository
    CoroutineScope(Dispatchers.IO).launch {
        val notiDao = NotiDatabase.getInstance(context).notiDao()
        repository = NotiRepository(notiDao)
        val noti : Noti = Noti.toEntity(notiDto)
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
}

fun deleteAllNotis(context: Context) {
    lateinit var repository: NotiRepository
    CoroutineScope(Dispatchers.IO).launch {
        val notiDao = NotiDatabase.getInstance(context).notiDao()
        repository = NotiRepository(notiDao)
        repository.deleteAllNotis()
    }
}