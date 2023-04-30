package com.slembers.alarmony.feature.common.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle


@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsTitle() {
    CardBox(
        title = { CardTitle(title = "장덕모임") },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 0.dp,
                        bottom = 10.dp,
                        end = 10.dp
                    ),
                content = {
                    CardDivider()
                    GroupDetailsRepeat()
                }
            )
        }
    )
}

@Composable
fun CardDivider() {
    Divider(
        thickness = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(
                color = Color(0xff9A9A9A),
                shape = MaterialTheme.shapes.extraSmall
            )
    )
}

@Composable
fun GroupDetailsText(
    text : String = "폰트",
    fontsize : TextUnit = 20.sp,
    color : Color = Color.Black
) {
    Text(
        text = text,
        style = TextStyle(
            color = color,
            fontSize = fontsize,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        textAlign = TextAlign.Start
    )
}

@Composable
fun GroupDetailsRepeat() {

    val week = remember { mutableStateListOf("월","화","수","목","금","토","일",) }

    Column(
        modifier = Modifier.padding(
            start = 20.dp,
            top = 0.dp,
            bottom = 0.dp,
            end = 0.dp
        ),
        content = {
            GroupDetailsText("오전")
            GroupDetailsText(text = "07:30", fontsize = 50.sp)
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(week) {
                        GroupDetailsText(
                            text = it,
                            color = Color.Gray
                        )
                    }
                }
            )
        }
    )
}

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsBoard() {
    CardBox(
        title = { CardTitle(
            title = "오늘 알람 기록",
            content =  {
                Text(
                    text = "23/04/19",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    ),
                    modifier = Modifier.padding(end = 10.dp),
                    textAlign = TextAlign.Start
                )
            }
        ) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 0.dp,
                        bottom = 10.dp,
                        end = 10.dp
                    ),
                content = {
                    CardDivider()
                }
            )
        }
    )
}
