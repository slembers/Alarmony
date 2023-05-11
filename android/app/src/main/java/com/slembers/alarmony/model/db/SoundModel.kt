package com.slembers.alarmony.model.db

import android.media.MediaPlayer
import androidx.compose.ui.graphics.painter.Painter

data class SoundItem(
    private val image: Painter? = null,
    private val name: String = "nothing",
    private val mp3Content: MediaPlayer? = null,
    private val mp3Id: Int = 0,
    private val imageId : Int = 0,


) {
    var soundImage = image
    var soundName = name
    var soundMp3Content = mp3Content
    var soundMp3Id = mp3Id
    var isPlaying = false
    var soundImageId = imageId
}