package com.slembers.alarmony.util;

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.slembers.alarmony.R
import com.slembers.alarmony.model.db.SoundItem

@Composable
fun groupSoundInfos() : List<SoundItem> = listOf(
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Normal",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.normal)
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Crescendo",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.crescendo)
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "SineLoop",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.sineloop)
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Piano",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.piano)
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Xylophone",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.xylophone)
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Guitar",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.guitar)
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Ukulele",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.ukulele)
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "RingRing",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.ringring)
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Chicken",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.chicken)
    ),
    SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Horror",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.horror)
    ),
)
