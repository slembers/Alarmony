package com.slembers.alarmony.model.db

import android.media.MediaPlayer
import androidx.compose.ui.graphics.painter.Painter

data class SoundItem(
    private val image: Painter? = null,
    private val name: String = "nothing",
    private val mp3Content: MediaPlayer? = null
) {
    var soundImage = image
    var soundName = name
    var soundMp3Content = mp3Content
}