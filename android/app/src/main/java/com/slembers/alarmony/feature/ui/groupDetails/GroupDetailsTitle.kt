package com.slembers.alarmony.feature.ui.groupDetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import com.slembers.alarmony.feature.alarm.Alarm
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardDivider
import com.slembers.alarmony.feature.common.CardTitle
import java.lang.Math.abs


@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsTitle(
    alarm : Alarm? = null,
) {
    val week = remember { mutableStateListOf("월","화","수","목","금","토","일",) }

    CardBox(
        title = { CardTitle(title = alarm?.title ?: "제목") },
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
                    Column(
                        modifier = Modifier.padding(
                            start = 20.dp,
                            top = 0.dp,
                            bottom = 0.dp,
                            end = 0.dp
                        ),
                        content = {
                            GroupDetailsText(if(alarm?.hour!! in 13..23) "오후" else "오전" )
                            GroupDetailsText(text = setTime(alarm.hour, alarm.minute), fontsize = 50.sp)
                            LazyRow(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                userScrollEnabled = false,
                                content = {
                                    items(week) {
                                        GroupDetailsText(
                                            text = it,
                                            color = GroupDetailsWeek(it)
                                        )
                                    }
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}

@Preview
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
                userScrollEnabled = false,
                content = {
                    items(week) {
                        GroupDetailsText(
                            text = it,
                            color = GroupDetailsWeek(it)
                        )
                    }
                }
            )
        }
    )
}

@Composable
fun GroupDetailsText(
    text : String = "월",
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

private fun GroupDetailsWeek(
    word : String = "월"
) : Color {
    return when (word) {
        "월" -> Color.Gray
        "화" -> Color.Gray
        "수" -> Color.Gray
        "목" -> Color.Gray
        "금" -> Color.Gray
        "토" -> Color.Red
        "일" -> Color.Red
        else -> {Color.Black}
    }
}

private fun setTime(_hour : Int, _minute : Int) : String {
    val hour = when(_hour){
        in 0..9 -> "0$_hour"
        in 10..12 -> "$_hour"
        in 13..21 -> "0${_hour - 12}"
        in 22..23 -> "${_hour - 12}"
        else -> {"00"}
    }
    val minute = if(_minute in 0..9) "0$_minute" else "$_minute"
    return "$hour:$minute"
}