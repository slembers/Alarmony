package com.slembers.alarmony.feature.screen

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.GroupCard
import com.slembers.alarmony.feature.common.ui.compose.GroupSubjet
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.feature.ui.group.GroupBottomButtom
import com.slembers.alarmony.feature.ui.group.GroupInvite
import com.slembers.alarmony.feature.ui.group.GroupSound
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.group.GroupTypeButton
import com.slembers.alarmony.feature.ui.group.GroupVolume
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.network.service.GroupService
import com.slembers.alarmony.viewModel.GroupViewModel

@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class GroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController : NavHostController = rememberNavController()
            val viewModel by viewModels<GroupViewModel>()
            NavHost(
                navController = navController,
                startDestination = NavItem.Group.route
            ) {
                composable( route = NavItem.Group.route ) { GroupScreen(
                    navController = navController, viewModel = viewModel) }
                composable( route = NavItem.GroupInvite.route ) { InviteScreen(
                    navController = navController, viewModel = viewModel) }
                composable( route = NavItem.Sound.route ) { SoundScreen(
                    navController = navController, viewModel = viewModel) }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("GroupActivity","[그룹생성] Activity 시작")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("GroupActivity","[그룹생성] Activity 종료")
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupScreen(
    navController : NavHostController = rememberNavController(),
    viewModel : GroupViewModel = viewModel()
) {

    val title by viewModel.title.observeAsState()
    val timePickerState by viewModel.alarmTime.observeAsState()
    val isWeeks by viewModel.currentWeeks.observeAsState()
    val weeks = listOf("월","화","수","목","금","토","일")
    val members by viewModel.members.observeAsState()
    val soundName by viewModel.sound.observeAsState()
    val vibration by viewModel.vibrate.observeAsState()
    val soundVolume by viewModel.volumn.observeAsState()

    val scrollerState = rememberScrollState()
    val context = LocalContext.current

    // 초대된 그룹원 확인
    val checkedMember = navController.previousBackStackEntry?.savedStateHandle?.get<Set<MemberDto>>("checkedMember")
    Log.d("checked","[그룹생성] 선택한 멤버 : ${checkedMember.toString()}")

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.Group.title,
                navClick = { (context as Activity).finish() }
            )
         },
        bottomBar = {
            GroupBottomButtom(
                text = "저장",
                onClick = {
                    Log.d("viewmodel:ID","[그룹생성] groupActivity ID : $viewModel")

                    GroupService.addGroupAlarm(
                        title = title,
                        hour = timePickerState?.hour ?: 7,
                        minute = timePickerState?.hour ?: 0,
                        alarmDate = weeks.map {
                            isWeeks?.getValue(it) ?: false
                        }.toList(),
                        members = members?.map { it.nickname }?.toList(),
                        soundName = soundName,
                        soundVolume = soundVolume,
                        vibrate = vibration,
                        context = context,
                        navController = navController
                    )
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
                        title = title!!,
                        onChangeValue = { viewModel.onChangeTitle(it) })
                    }
                )
                GroupCard(
                    title = { GroupTitle(title = "알람시간") },
                    content = {
                        TimeInput(
                            state = timePickerState!!,
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
                    members = members ?: mutableListOf()
                )
                GroupSound(
                    navController = navController,
                    sound = soundName,
                )
                GroupTypeButton(
                    isVibrate = vibration ?: true,
                    viewModel = viewModel
                )
                GroupVolume(
                    volume = soundVolume ?: 7f,
                    setVolume = { viewModel.onChangeVolume(it) }
                )
            }
        }
    )
}