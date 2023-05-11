package com.slembers.alarmony.feature.notification

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotiDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // 만약 동일한 PrimaryKey 가 있을 경우 덮어쓰기
    suspend fun insertNoti(noti: Noti)

    @Query("SELECT * FROM notis")
    fun getAllNotis(): LiveData<List<Noti>>

    @Query("SELECT * FROM notis WHERE notiId=:notiId")
    fun getNotiById(notiId: Long): Noti?

    @Delete
    suspend fun deleteNoti(noti: Noti)

    @Query("DELETE FROM notis")
    suspend fun deleteAllNotis()
}
