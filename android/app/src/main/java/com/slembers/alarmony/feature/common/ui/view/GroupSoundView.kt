package com.slembers.alarmony.feature.common.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.slembers.alarmony.R
import com.slembers.alarmony.model.db.SoundItem


@Preview
@Composable
fun soundIconView(
    image : Painter? = painterResource(id = R.drawable.main_app_image_foreground),
    soundname : String = "nothing",
    onClick : Boolean = false,
    checkBox : ((String) -> Unit) =  {}
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)
            .shadow(
                elevation = 5.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                shape = RoundedCornerShape(20.dp)
            )
            .clip(MaterialTheme.shapes.medium)
            .background(
                if (!onClick)
                    MaterialTheme.colorScheme.background
                else
                    MaterialTheme.colorScheme.primary
            )
            .fillMaxWidth()
            .clickable {
                checkBox(soundname)
                Log.i("$soundname", "checkbox : ${checkBox.toString()}")
            },
        content = {
            Image(
                modifier = Modifier
                    .size(
                        width = this.maxWidth,
                        height = this.maxHeight
                    )
                    .padding(5.dp),
                painter = painterResource(id = R.drawable.main_app_image_foreground),
                contentDescription = null)
        }
    )
}

@Preview
@Composable
fun SoundChooseGridView(
    size : Dp = 320.dp,
    soundItems : List<SoundItem> = (1..10).map {
        SoundItem(
            painterResource(id = R.drawable.main_app_image_foreground),
            "sound$it") }.toList()
) {

    var checkbox by remember { mutableStateOf(soundItems[0].soundName) }

    LazyVerticalGrid(
        modifier = Modifier.width(320.dp),
        columns = GridCells.Adaptive(minSize = 100.dp),
    ) {
        items(soundItems) {
            soundIconView(
                image = it.soundImage,
                soundname = it.soundName,
                onClick = checkbox.equals(it.soundName),
                checkBox = { checkbox = it }
            )
        }
    }
}