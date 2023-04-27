package com.slembers.alarmony.feature.common.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.slembers.alarmony.R


@Preview
@Composable
fun soundIcon() {
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
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth(),
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
    size : Dp = 320.dp
) {
    LazyVerticalGrid(
        modifier = Modifier.width(320.dp),
        columns = GridCells.Adaptive(minSize = 100.dp),
        content = {
            items(count = 40) {
                soundIcon()
            }
        })
}