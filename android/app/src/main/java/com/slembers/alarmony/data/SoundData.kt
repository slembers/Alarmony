package com.slembers.alarmony.feature.group

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.slembers.alarmony.R
import com.slembers.alarmony.model.db.SoundItem

@Composable
fun groupSoundItems() : List<SoundItem> = listOf(
    SoundItem(
        image = painterResource(id = R.drawable.main_app_image_foreground),
        name = "sound01"
    ),
    SoundItem(
        image = painterResource(id = R.drawable.main_app_image_foreground),
        name = "sound02"
    ),
    SoundItem(
        image = painterResource(id = R.drawable.main_app_image_foreground),
        name = "sound03"
    ),
    SoundItem(
        image = painterResource(id = R.drawable.main_app_image_foreground),
        name = "sound04"
    ),
    SoundItem(
        image = painterResource(id = R.drawable.main_app_image_foreground),
        name = "sound05"
    ),
    SoundItem(
        image = painterResource(id = R.drawable.main_app_image_foreground),
        name = "sound06"
    ),
    SoundItem(
        image = painterResource(id = R.drawable.main_app_image_foreground),
        name = "sound07"
    )
)