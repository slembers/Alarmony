package com.slembers.alarmony.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.Display

object DisplayDpUtil {

    fun px2dp(px : Int, context : Context) : Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_HIGH)
    }
}