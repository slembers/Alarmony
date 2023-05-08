package com.slembers.alarmony.feature.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.GroupCard
import com.slembers.alarmony.feature.common.ui.compose.GroupSubjet
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.feature.ui.group.GroupBottomButtom
import com.slembers.alarmony.feature.ui.group.GroupInvite
import com.slembers.alarmony.feature.ui.group.GroupSound
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.group.GroupVolume
import com.slembers.alarmony.model.db.Group
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.network.service.GroupService
import com.slembers.alarmony.viewModel.GroupViewModel
import kotlin.streams.toList

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupScreen(
    navController : NavHostController = rememberNavController(),
    viewModel : GroupViewModel = viewModel()
) {

    val title by viewModel.title.observeAsState("")
    val timePickerState by viewModel.alarmTime.observeAsState(
        TimePickerState(10,0,false)
    )
    val isWeeks by viewModel.currentWeeks.observeAsState()
    val weeks = listOf("월","화","수","목","금","토","일")
    val members by viewModel.members.observeAsState(listOf<MemberDto>())
    val soundName by viewModel.sound.observeAsState("노래제목")
    val vibration by viewModel.vibrate.observeAsState(true)
    val soundVolume by viewModel.volumn.observeAsState(7f)

    val scrollerState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.Group.title,
                navcontroller = navController
            )
         },
        bottomBar = {
            GroupBottomButtom(
                text = "저장",
                onClick = {
                    var connection = false
                    GroupService.addGroupAlarm(
                        Group(
                            title = title,
                            hour = timePickerState.hour,
                            minute = timePickerState.minute,
                            alarmDate = weeks.stream().map {
                                isWeeks?.getValue(it) ?: false
                            }.toList(),
                            members = members?.stream()?.map {
                                it.nickname
                            }?.toList(),
                            soundName = soundName,
                            soundVolume = soundVolume,
                            vibrate = vibration
                        ),
                        connection = { connection = it}
                    )
                    if(connection)
                        Toast.makeText(context,"정상적으로 저장하였습니다.",Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(context,"저장에 실패하였습니다.",Toast.LENGTH_SHORT).show()
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(10.dp)
                    .verticalScroll(scrollerState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GroupCard(
                    title = { GroupTitle(title = "그룹제목") },
                    content = { GroupSubjet(
                        title = title,
                        onChangeValue = { viewModel.onChangeTitle(it) })
                    }
                )
                GroupCard(
                    title = { GroupTitle(title = "알람시간") },
                    content = {
                        TimeInput(
                            state = timePickerState,
                            modifier = Modifier
                                .padding(
                                    start = 20.dp,
                                    top = 10.dp,
                                    bottom = 0.dp,
                                    end = 0.dp
                                )
                                .focusable(false)
                        )
                    }
                )
                GroupCard(
                    title = { GroupTitle(title = "요일선택") },
                    content = {
                        BoxWithConstraints(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val boxSize = this.maxWidth / 8
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        top = 0.dp,
                                        bottom = 0.dp,
                                        end = 10.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                items(weeks) {item ->
                                    TextButton(
                                        modifier = Modifier.size(boxSize),
                                        onClick = {
                                            val change = !viewModel.getIsWeek(item)
                                            viewModel.onChangeWeek(item, change)
                                            Log.d("click event","[그룹생성] : $item value : ${viewModel.getIsWeek(item)}")
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.Black,
                                            containerColor =
                                            viewModel.getIsWeek(item).let {
                                                if(it) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    MaterialTheme.colorScheme.background
                                                }
                                            }
                                        ),
                                        content = {
                                            Text( text = item )
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
                GroupInvite(
                    navController = navController,
                    members = members ?: listOf()
                )
                GroupSound(
                    navController = navController,
                    sound = soundName,
                )
                GroupCard(
                    title = { GroupTitle(
                        title = "타입",
                        content = {
                            Row(
                                modifier = Modifier
                                    .height(50.dp)
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                BoxWithConstraints(
                                    modifier = Modifier.fillMaxHeight(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .size(this.maxHeight)
                                            .align(Alignment.Center)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.primary)
                                    ) {
                                        Image(
                                            modifier = Modifier.align(Alignment.Center),
                                            painter = painterResource(id = R.drawable.baseline_music_note_24) ,
                                            contentDescription = null)
                                    }
                                }

                                BoxWithConstraints(
                                    modifier = Modifier.fillMaxHeight(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .padding(2.dp)
                                            .size(this.maxHeight)
                                            .align(Alignment.Center)
                                            .clip(CircleShape)
                                            .background(
                                                if (vibration == true)
                                                    MaterialTheme.colorScheme.primary
                                                else
                                                    MaterialTheme.colorScheme.background
                                            )
                                            .clickable {
                                                vibration?.let {
                                                    viewModel.onChangeVibrate(!it)
                                                    Log.i("vibration", "vibration value : $it")
                                                }
                                            }
                                    ) {
                                        Image(
                                            modifier = Modifier.align(Alignment.Center),
                                            painter = painterResource(id = R.drawable.baseline_vibration_24) ,
                                            contentDescription = null)
                                    }
                                }
                            }
                        }
                    )},
                )
                GroupVolume(
                    volume = soundVolume,
                    setVolume = { viewModel.onChangeVolume(it) }
                )
            }
        }
    )
}