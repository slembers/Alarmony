package com.slembers.alarmony.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log

object WifiUtil {
    fun isNetworkConnected(application: Application) : Boolean {
        val manager : ConnectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        Log.d("wifiUtil","[연결확인]네트워크 연결상태 확인")
        // 안드로이드 29부터 NetworkInfo는 Deprecated 되었음
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capability : NetworkCapabilities? = manager.getNetworkCapabilities(manager.activeNetwork)
            if(capability != null )  {
                // WIFI or 인터넷이 연결되어있는지 확인
                if(capability.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capability.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    return true
                } else if(capability.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    return false
                }
            }
        } else { // 안드로이드 29미만 버젼일 경우
            // WIFI연결일 경우
            val wifi : NetworkInfo? = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            // 모바일일 경우
            val mobile : NetworkInfo? = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            // wimax
            val wimax : NetworkInfo? = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX)
            var bwimax : Boolean = false
            if(wimax != null) {
                bwimax = wimax.isConnected
            }
            if( mobile != null ) {
                if( mobile.isConnected || wifi!!.isConnected || bwimax) {
                    return true
                }
            } else {
                if( wifi!!.isConnected || bwimax ) {
                    return true
                }
            }
        }
        return false
    }
}
