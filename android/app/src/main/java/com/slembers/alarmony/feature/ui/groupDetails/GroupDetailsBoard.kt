package com.slembers.alarmony.feature.ui.groupDetails

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardDivider
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.model.db.Record
import com.slembers.alarmony.network.service.GroupService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsBoard(
    items: Map<String, List<Record>> = hashMapOf(
        "success" to listOf(),
        "failed" to listOf()
    ),
    groupId: Long = 0
) {
    CardBox(
        title = { CardTitle(
            title = "오늘 알람 기록",
            content =  {
                Text(
                    text = currentDate(),
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
                    if(items.getValue("success").isEmpty()) {

                    } else {
                        LazyColumn(
                            modifier = Modifier.height(200.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {
                                items(items.getValue("success")) {
                                    MemberDetails(
                                        nickname = it.nickname,
                                        profile = it.profileImg,
                                        isCheck = it.success
                                    )
                                }
                            }
                        )
                    }
                    CardDivider()
                    if(items.getValue("failed").isEmpty()) {

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

private fun currentDate() : String {
    val local = LocalDate.now(ZoneId.of("Asia/Seoul"))
    val dateTimeFormat = DateTimeFormatter.ofPattern("yy/MM/dd")
    return local.format(dateTimeFormat)
}