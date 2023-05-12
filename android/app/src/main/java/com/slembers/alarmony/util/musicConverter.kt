package com.slembers.alarmony.util

import android.content.Context
import com.slembers.alarmony.R

fun soundConverter(context : Context, soundName : String) : Int {
    val mySound = when(soundName) {
        "Crescendo" -> R.raw.crescendo
        "SineLoop" -> R.raw.sineloop
        "Piano" -> R.raw.piano
        "Xylophone" -> R.raw.xylophone
        "Guitar" -> R.raw.guitar
        "Ukulele" -> R.raw.ukulele
        "RingRing" -> R.raw.ringring
        "Chicken" -> R.raw.chicken
        "Horror" -> R.raw.horror
        else -> R.raw.normal
    }
    return mySound
}