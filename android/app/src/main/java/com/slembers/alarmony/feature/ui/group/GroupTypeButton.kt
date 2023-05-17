package com.slembers.alarmony.feature.ui.group

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.ui.compose.GroupCard
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.viewModel.GroupViewModel

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupTypeButton(
    isSound: Boolean = true,
    isVibrate: Boolean = true,
    viewModel: GroupViewModel = viewModel()
) {

    Row(
        modifier = Modifier
            .height(50.dp)
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(horizontalArrangement = Arrangement.Start) {
            Text(
                "진동 여부",
                textAlign = TextAlign.Start

            )
        }
        Spacer(modifier = Modifier.weight(1f))

      /*  BoxWithConstraints(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(this.maxHeight)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background("#7DC3F2".toColor())
            ) {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.baseline_music_note_24),
                    contentDescription = null
                )
            }
        }*/

        BoxWithConstraints(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(35.dp)
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(
                        if (isVibrate)
                            "#7DC3F2".toColor()
                        else
                            "#dcdcdc".toColor()
                    )
                    .clickable {
                        viewModel.onChangeVibrate(!isVibrate)
                        Log.d("vibration", "vibration value : $isVibrate")
                    }
            ) {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    painter = painterResource(id = R.drawable.baseline_vibration_24),
                    contentDescription = null
                )
            }
        }
    }
}