package com.slembers.alarmony.feature.ui.group

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.model.db.SoundItem
import com.slembers.alarmony.util.Sound


@Composable
fun soundIconView(
    soundItem : SoundItem = SoundItem(
        image = painterResource(id = R.drawable.play_button),
        name = "Normal",
        mp3Content = MediaPlayer.create(LocalContext.current, R.raw.normal),
        imageId = R.drawable.play_button
    ),
    onClick : Boolean = false,
    checkBox : ((SoundItem) -> Unit) =  {},
    currentPlayer : () -> Unit = {}
) {
    var imageResId by remember {mutableStateOf(
        if (soundItem.isPlaying) {
            R.drawable.pause_button
        } else {
            R.drawable.play_button
        })
    }
    val playButton = painterResource(id = R.drawable.play_button)
    val pauseButton = painterResource(id = R.drawable.pause_button)
    fun changeImage() {
        if (!soundItem.isPlaying) {
            soundItem.isPlaying = true
            soundItem.soundMp3Content?.start()
            soundItem.soundImage = pauseButton
            imageResId = R.drawable.pause_button
            Log.d("INFO", "${soundItem.soundName} 재생 시작")
        } else {
            soundItem.isPlaying = false
            soundItem.soundMp3Content?.pause()
            soundItem.soundImage = playButton
            imageResId = R.drawable.play_button
            Log.d("INFO", "${soundItem.soundName} 재생 끝")
        }
    }
    BoxWithConstraints(
        modifier = Modifier
            .padding(8.dp)
            .width(350.dp)
            .height(90.dp)
            .shadow(
                elevation = 5.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                shape = RoundedCornerShape(25.dp)
            )
            .clip(MaterialTheme.shapes.medium)
            .background(
                if (!onClick)
                    MaterialTheme.colorScheme.background
                else
                    MaterialTheme.colorScheme.primary
            )
            .clickable {
                checkBox(soundItem)
                Log.i("$soundItem.soundName", "checkbox : ${checkBox.toString()}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text(
                text = soundItem.soundName,
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
            )
            Image(
                contentDescription = null,
                modifier = Modifier.clickable(onClick = currentPlayer),
                painter = soundItem.soundImage!!
            )
        }
    }
}

@Composable
fun soundIconView2(
    soundItem : Sound,
    onClick : Boolean = false,
    checkBox : ((Sound) -> Unit) =  {},
    isPlaySound: Boolean = false,
    currentPlayer : () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
            .padding(8.dp)
            /*.width(350.dp)
            .height(40.dp)*/
            .shadow(
                elevation = 5.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                shape = RoundedCornerShape(25.dp)
            )
            .clip(MaterialTheme.shapes.medium)
            .background(
                if (!onClick)
                    "#FFFFFF".toColor()
                else
                    "#E9E9E9".toColor()
            )
            .clickable {
                checkBox(soundItem)
                Log.i("$soundItem.soundName", "checkbox : ${checkBox.toString()}")
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
        )
        {
            Text(
                text = soundItem?.soundName ?: "Normal",
                modifier = Modifier
                    .align(Alignment.CenterVertically),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                   // fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
            )
            Image(
                contentDescription = null,
                modifier = Modifier.clickable(onClick = currentPlayer).size(width = 25.dp, height = 25.dp),
                painter =
                    if(isPlaySound)
                        painterResource(id = R.drawable.pause_button)
                    else
                        painterResource(id = R.drawable.play_button)
            )
        }
    }
}