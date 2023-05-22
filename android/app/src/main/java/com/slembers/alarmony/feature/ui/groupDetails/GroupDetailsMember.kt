package com.slembers.alarmony.feature.ui.groupDetails

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.slembers.alarmony.R
import com.slembers.alarmony.network.service.GroupService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

@Composable
fun MemberDetails(
    profile: String? = null,
    nickname: String,
    message: String = "체크하지 않았어요...",
    isCheck: Boolean = false,
    host: Boolean = false,
    alarmId: Long,
    onAlarm: MutableState<Boolean> = mutableStateOf(true),
    remainingSec: MutableState<Int> = mutableStateOf(60)
) {
//    val viewModel: SendAlarmButtonViewModel = viewModel(key = "${alarmId}_${nickname}") {
//        SendAlarmButtonViewModel()
//    }
//    var onAlarm = viewModel.onAlarm
//    var remainingSec = viewModel.remainingSec

//    val onAlarm = rememberSaveable { mutableStateOf(true) }
//    val remainingSec = rememberSaveable { mutableStateOf(60) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 2.dp, end = 2.dp, top = 3.dp, bottom = 1.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .heightIn(
                minOf(50.dp),
                maxOf(80.dp)
            )
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .padding(0.dp),
            content = {
                if(profile != null ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(profile)
                            .build(),
                        contentDescription = "ImageRequest example",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.baseline_account_circle_48)
                    )
                }
                else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_account_circle_48),
                        contentDescription = "ImageRequest example",
                        modifier = Modifier
                            .clip(CircleShape)
                            .align(Alignment.Center)
                            .fillMaxSize()
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = nickname,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            if(message.isNotBlank()){
                Text(
                    text = message,
                    fontSize = 10.sp,
                    modifier = Modifier.fillMaxWidth(),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

        }
        if(host && onAlarm.value!! && !isCheck) {
            GroupDetailsBoardBtn(
                isCheck = false,
                onClick = {
                    Log.d("myResponse", "${alarmId}_${nickname}")
                    onAlarm.value = false
                    CoroutineScope(Dispatchers.IO).async {
                        GroupService.notification(
                            alarmId,
                            nickname
                        )
                    }
                },
                onAlarm = onAlarm,
                remainingSec = remainingSec,
            )
        }
        else if(host && !onAlarm.value!! &&!isCheck) {
            GroupDetailsBoardBtn(
                isCheck = false,
                onAlarm = onAlarm,
                remainingSec = remainingSec,
            )
        }
        else if(!host && isCheck){
            GroupDetailsBoardBtn(
                isCheck = true,
                onAlarm = onAlarm,
                remainingSec = remainingSec,
            )
        }
        else if(host && isCheck){
            GroupDetailsBoardBtn(
                isCheck = true,
                onAlarm = onAlarm,
                remainingSec = remainingSec,
            )
        }
    }
}