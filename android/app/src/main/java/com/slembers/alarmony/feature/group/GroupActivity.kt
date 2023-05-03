package com.slembers.alarmony.feature.group

import android.util.Log
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.GroupCard
import com.slembers.alarmony.feature.common.ui.compose.GroupSubjet
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.network.service.GroupService
import com.slembers.alarmony.viewModel.GroupViewModel

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupScreen(
    navController : NavHostController = rememberNavController(),
    groupViewModel : GroupViewModel = viewModel()
) {

    val title by groupViewModel.title.observeAsState("")
    val timePickerState by groupViewModel.alarmTime.observeAsState(
        TimePickerState(14,0,false)
    )
    val groupmembers by groupViewModel.groupMember.observeAsState()
    val isWeeks = remember{ mutableStateMapOf(
        "월" to true,
        "화" to true,
        "수" to true,
        "목" to true,
        "금" to true,
        "토" to true,
        "일" to true
    )}
    val weeks = remember{ mutableStateListOf("월","화","수","목","금","토","일") }
    var soundStatus by remember { mutableStateOf(true) }
    val vibration by groupViewModel.vibration.observeAsState(true)
    val soundName by groupViewModel.sound.observeAsState("노래제목")
    val soundVolume by groupViewModel.volumn.observeAsState(7f)

    val scrollerState = rememberScrollState()

    var notification = "\"this is text : ${groupViewModel.title.value} " +
            "\n hour : ${groupViewModel.alarmTime.value?.hour}" +
            "\n minue : ${groupViewModel.alarmTime.value?.minute}" +
            "\n ampm : ${groupViewModel.alarmTime.value?.is24hour}" +
            "\n vibration : ${groupViewModel.vibration}" +
            "\n sound : ${groupViewModel.sound}" +
            "\n volumn : ${groupViewModel.volumn}"

    val context = LocalContext.current

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.Group.title,
                navEvent = navController
            )
         },
        bottomBar = {
            GroupBottomButtom(
                text = "저장",
                onClick = {
                    GroupService.addGroupAlarm()
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
                        onChangeValue = { groupViewModel.onChangeTitle(it) })
                    }
                )
                GroupCard(
                    title = { GroupTitle(title = "알람시간") },
                    content = {
                        TimeInput(
                            state = timePickerState,
                            modifier = Modifier.padding(
                                start = 20.dp,
                                top = 10.dp,
                                bottom = 0.dp,
                                end = 0.dp
                            ).focusable(false)
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
                                items(weeks) {
                                    TextButton(
                                        modifier = Modifier.size(boxSize),
                                        onClick = {
                                            isWeeks[it] = !isWeeks.getValue(it)
                                            Log.d("click event","[그룹생성] : $it value : ${isWeeks[it]}")
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            contentColor = Color.Black,
                                            containerColor =
                                            if(isWeeks.getValue(it)) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.background
                                            },
                                        ),
                                        content = {
                                            Text( text = it )
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
                GroupInvite(
                    navController = navController,
                    members = groupmembers ?: listOf()
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
                                                    groupViewModel.onChangeVibration(!it)
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
                    setVolume = { groupViewModel.onChangeVolumn(it)}
                )
            }
        }
    )
}