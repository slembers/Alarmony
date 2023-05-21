package com.slembers.alarmony.feature.notification

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Noti::class], version = 3, exportSchema = false)
abstract class NotiDatabase : RoomDatabase() {
    abstract fun notiDao(): NotiDao

    companion object {
        @Volatile
        private var INSTANCE: NotiDatabase? = null

        fun getInstance(context: Context): NotiDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotiDatabase::class.java,
                    "noti_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                return INSTANCE!!
            }
        }
    }
}