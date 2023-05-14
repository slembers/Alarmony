package com.slembers.alarmony.util

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.slembers.alarmony.R
import com.slembers.alarmony.model.db.SoundItem

@Composable
fun groupSoundInfos() : List<SoundItem> = listOf(
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Normal",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.normal),
        mp3Id = R.raw.normal,
        imageId = R.drawable.play_button
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Crescendo",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.crescendo),
        mp3Id = R.raw.crescendo,
        imageId = R.drawable.play_button
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "SineLoop",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.sineloop),
        mp3Id = R.raw.sineloop,
        imageId = R.drawable.play_button
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Piano",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.piano),
        mp3Id = R.raw.piano,
        imageId = R.drawable.play_button
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Xylophone",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.xylophone),
        mp3Id = R.raw.xylophone,
        imageId = R.drawable.play_button
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Guitar",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.guitar),
        mp3Id = R.raw.guitar,
        imageId = R.drawable.play_button
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Ukulele",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.ukulele),
        mp3Id = R.raw.ukulele,
        imageId = R.drawable.play_button
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "RingRing",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.ringring),
        mp3Id = R.raw.ringring,
        imageId = R.drawable.play_button
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Chicken",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.chicken),
        mp3Id = R.raw.chicken,
        imageId = R.drawable.play_button
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Horror",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.horror),
        mp3Id = R.raw.horror,
        imageId = R.drawable.play_button
    ),
)

data class Sound(
    private val name: String = "Normal",
    private val mp3Id: Int = 0,
    private var imageId : Int = 0,
) {

    val soundName = name
    val soundMp3Id = mp3Id
    val soundImageId = imageId

    fun soundList() = mutableStateListOf(

        Sound(
            name = "Normal",
            mp3Id = R.raw.normal,
            imageId = R.drawable.play_button
        ),
        Sound(
                name = "Crescendo",
        mp3Id = R.raw.crescendo,
        imageId = R.drawable.play_button
        ),
        Sound(
            name = "SineLoop",
            mp3Id = R.raw.sineloop,
            imageId = R.drawable.play_button
        ),
        Sound(
            name = "Piano",
            mp3Id = R.raw.piano,
            imageId = R.drawable.play_button
        ),
        Sound(
            name = "Xylophone",
            mp3Id = R.raw.xylophone,
            imageId = R.drawable.play_button
        ),
        Sound(
            name = "Guitar",
            mp3Id = R.raw.guitar,
            imageId = R.drawable.play_button
        ),
        Sound(
            name = "Ukulele",
            mp3Id = R.raw.ukulele,
            imageId = R.drawable.play_button
        ),
        Sound(
            name = "RingRing",
            mp3Id = R.raw.ringring,
            imageId = R.drawable.play_button
        ),
        Sound(
            name = "Chicken",
            mp3Id = R.raw.chicken,
            imageId = R.drawable.play_button
        ),
        Sound(
            name = "Horror",
            mp3Id = R.raw.horror,
            imageId = R.drawable.play_button
        ),
    )

    fun onchangeImage(imageId : Int) {
        this.imageId = imageId
    }

}

