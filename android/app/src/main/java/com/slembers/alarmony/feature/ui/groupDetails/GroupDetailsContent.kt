package com.slembers.alarmony.feature.ui.groupDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.alarm.Alarm
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardDivider
import com.slembers.alarmony.feature.common.CardTitle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Preview
@Composable
fun GroupDetailsContent(
    alarm : Alarm?= null,
) {
    CardBox(
        title = { CardTitle(title = "알람소개") },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 0.dp,
                        bottom = 10.dp,
                        end = 20.dp
                    ),
                content = {
                    CardDivider(color = Color(0xff9A9A9A))
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = if (alarm!!.content.isNullOrEmpty()) Alignment.Center else Alignment.TopStart

                    ) {
                        Text(
                            text = if (alarm?.content.isNullOrEmpty()) {
                                "알람소개 내용이 없습니다."
                            } else {
                                alarm!!.content
                            },
                            modifier = Modifier.padding(top = 15.dp, bottom = 10.dp),
                        )
                    }
                }
            )
        }
    )
}


