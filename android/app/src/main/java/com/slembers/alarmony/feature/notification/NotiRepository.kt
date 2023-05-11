package com.slembers.alarmony.feature.notification

import androidx.lifecycle.LiveData

class NotiRepository(private val notiDao: NotiDao) {
    val readAllData : LiveData<List<Noti>> = notiDao.getAllNotis()

    fun findNoti(notiId : Long) : Noti? {
        val noti : Noti? = notiDao.getNotiById(notiId)
        return noti
    }

    suspend fun addNoti(noti: Noti) {
        notiDao.insertNoti(noti)
    }

    suspend fun deleteNoti(noti: Noti) {
        notiDao.deleteNoti(noti)
    }

    suspend fun deleteAllNotis() {
        notiDao.deleteAllNotis()
    }
}