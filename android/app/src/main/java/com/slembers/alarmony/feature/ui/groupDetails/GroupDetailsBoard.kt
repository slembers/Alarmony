package com.slembers.alarmony.feature.ui.groupDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardDivider
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.model.db.Record
import com.slembers.alarmony.network.service.GroupService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsBoard(
    items: Map<String, List<Record>> = hashMapOf(
        "success" to listOf(),
        "failed" to listOf()
    ),
    groupId: Long = 0,
    host : Boolean = false
) {
    CardBox(
        title = { CardTitle(
            title = "오늘의 알람현황",
            content =  {
                Text(
                    text = currentDate().format(),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                       // fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    ),
                    modifier = Modifier.padding(end = 15.dp),
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
                        bottom = 0.dp,
                        end = 20.dp
                    ),
                content = {
                    CardDivider(color = "#989898".toColor())
                    if(items.getValue("success").isEmpty()) {
                        nothingItem(content = "오늘 알람을 확인 인원이 없습니다.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.height(200.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {
                                items(items.getValue("success")) {
                                    MemberDetails(
                                        nickname = it.nickname,
                                        profile = it.profileImg,
                                        message = "",
                                        isCheck = it.success
                                    )
                                }
                            }
                        )
                    }
                    CardDivider(color = "#E9E9E9".toColor())
                    if(items.getValue("failed").isEmpty()) {
                        nothingItem(content = "모두 일어났어요.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.height(200.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {
                                items(items.getValue("failed")) {
                                    MemberDetails(
                                        nickname = it.nickname,
                                        profile = it.profileImg,
                                        isCheck = it.success,
                                        message = it.message ?: "No checking..",
                                        host = host,
                                        onClick = {
                                            if(it.success.not()) {
                                                CoroutineScope(Dispatchers.IO).async {
                                                    GroupService.notification(
                                                        groupId,
                                                        it.nickname
                                                    )
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            )
        }
    )
}

@Preview
@Composable
fun nothingItem(
    content: String = ""
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
        /*    Image(
//                modifier = Modifier.align(Alignment.Center),
                painter = painterResource( id = R.drawable.mascot_foreground),
                contentDescription = null
            )*/
            Text(
                text = content,
                modifier = Modifier.weight(1f).padding(top = 20.dp , bottom = 20.dp),
                textAlign = TextAlign.Center
            )
        }
    )
}

private fun currentDate() : String {
    val local = LocalDate.now(ZoneId.of("Asia/Seoul"))
    val dateTimeFormat = DateTimeFormatter.ofPattern("M월 d일 E요일")
    return local.format(dateTimeFormat)
}