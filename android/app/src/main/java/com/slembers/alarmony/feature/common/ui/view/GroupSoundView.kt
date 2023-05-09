package com.slembers.alarmony.feature.common.ui.view

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slembers.alarmony.R
import com.slembers.alarmony.model.db.SoundItem
import com.slembers.alarmony.util.groupSoundInfos


@Preview
@Composable
fun soundIconView(
    image : Painter? = painterResource(id = R.drawable.play_button),
    soundName : String = "Normal",
    soundContent : MediaPlayer? = MediaPlayer.create(LocalContext.current, R.raw.normal),
    onClick : Boolean = false,
    checkBox : ((String) -> Unit) =  {}
) {
    var imageResId by remember { mutableStateOf(R.drawable.play_button) }
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
                checkBox(soundName)
                Log.i("$soundName", "checkbox : ${checkBox.toString()}")
            },
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Text(
                    text = soundName,
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
                    painter = painterResource(id = imageResId),
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        if (imageResId == R.drawable.play_button) {
                            soundContent?.start()
                            imageResId = R.drawable.pause_button
                        } else {
                            soundContent?.pause()
                            imageResId = R.drawable.play_button
                        }
                    }
                )
            }
        }
    )
}

@Preview
@Composable
fun SoundChooseGridView(
    size : Dp = 320.dp,
    soundItems : List<SoundItem> = groupSoundInfos()
) {
    var checkbox by remember { mutableStateOf(soundItems[0].soundName) }
    LazyVerticalGrid(
        modifier = Modifier.width(350.dp),
        columns = GridCells.Adaptive(minSize = 300.dp),
    ) {
        items(soundItems) {
            soundIconView(
                image = it.soundImage,
                soundName = it.soundName,
                soundContent = it.soundMp3Content,
                onClick = checkbox.equals(it.soundName),
                checkBox = { checkbox = it }
            )
        }
    }
}