package com.slembers.alarmony.feature.ui.groupDetails

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.MainActivity.Companion.viewModelStore
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardDivider
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.model.db.Record
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
                        nothingItem(content = "알람을 확인한 인원이 없습니다.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.heightIn(30.dp,540.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {
                                items(items.getValue("success")) {
                                    MemberDetails(
                                        nickname = it.nickname,
                                        profile = it.profileImg,
                                        message = "",
                                        host = host,
                                        alarmId = groupId,
                                        isCheck = it.success,
                                    )
                                }
                            }
                        )
                    }
                    CardDivider(color = "#E9E9E9".toColor())
                    if(items.getValue("failed").isEmpty()) {
                        nothingItem(content = "모든 인원이 알람을 확인하였습니다.")
                    } else {
                        LazyColumn(
                            modifier = Modifier.heightIn(30.dp,540.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            content = {
                                items(items.getValue("failed")) {
                                    lateinit var onAlarm : MutableState<Boolean>
                                    lateinit var remainingSec : MutableState<Int>
                                    val viewModel = SendAlarmButtonViewModel()
                                    val viewModelKey = "${groupId}_${it.nickname}"
                                    var existingViewModel = viewModelStore.get(viewModelKey)
                                    Log.d("myResponse_existingViewModel", existingViewModel.toString())
                                    Log.d("myResponse_existingViewModel", viewModelKey.toString())
                                    if (existingViewModel == null) {
                                        viewModelStore.put(viewModelKey, viewModel)
                                        onAlarm = viewModel.onAlarm
                                        remainingSec = viewModel.remainingSec
                                    } else {
                                        existingViewModel = viewModelStore.get(viewModelKey) as SendAlarmButtonViewModel
                                        onAlarm = existingViewModel.onAlarm
                                        remainingSec = existingViewModel.remainingSec
                                    }
                                    MemberDetails(
                                        nickname = it.nickname,
                                        profile = it.profileImg,
                                        message = it.message ?: "",
                                        host = host,
                                        alarmId = groupId,
                                        isCheck = it.success,
                                        onAlarm = onAlarm,
                                        remainingSec = remainingSec
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
            .padding(top = 30.dp, bottom = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = {
        /*    Image(
//                modifier = Modifier.align(Alignment.Center),
                painter = painterResource( id = R.drawable.mascot_foreground),
                contentDescription = null
            )*/
            Text(
                text = content,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 20.dp, bottom = 20.dp),
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