package com.slembers.alarmony.feature.ui.group


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.ui.compose.GroupCard
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.feature.common.ui.theme.toColor

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi

fun GroupVolume(
    volume: Float,
    setVolume: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .height(50.dp)
            .padding(5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "볼륨", modifier = Modifier
                .weight(0.6f),
            textAlign = TextAlign.Start
        )


        Slider(
            value = volume,
            onValueChange = setVolume,
            valueRange = 0f..15f,
            enabled = true,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.background,
                activeTrackColor = "#7DC3F2".toColor()
            ),
            modifier = Modifier.weight(0.4f)
        )
    }
}
