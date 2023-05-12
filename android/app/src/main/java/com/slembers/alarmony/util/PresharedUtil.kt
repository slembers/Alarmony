package com.slembers.alarmony.util

import android.content.SharedPreferences
import android.content.Context

class PresharedUtil(context : Context) {

    private val prefs : SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String?) : String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key : String, str : String?) {
        prefs.edit().putString(key,str).apply()
    }


    fun setBoolean(key: String, value: Boolean?) {
        prefs.edit().putBoolean(key, value ?: false).apply()
    }

    fun getBoolean(key : String,  value: Boolean) : Boolean {
        return prefs.getBoolean(key, value)
    }

    fun reset() {
        prefs.edit().clear().apply()
    }


}