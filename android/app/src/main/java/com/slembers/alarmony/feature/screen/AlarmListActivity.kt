package com.slembers.alarmony.feature.screen

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.alarm.Alarm
import com.slembers.alarmony.feature.alarm.AlarmDto
import com.slembers.alarmony.feature.alarm.AlarmViewModel
import com.slembers.alarmony.feature.alarm.AlarmViewModelFactory
import com.slembers.alarmony.feature.alarm.saveTestAlarm
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.notosanskr
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.notification.NotiViewModel
import com.slembers.alarmony.feature.notification.NotiViewModelFactory

@ExperimentalGlideComposeApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmListScreen(navController : NavHostController) {
    val context = LocalContext.current
    val mAlarmViewModel : AlarmViewModel = viewModel(
        factory = AlarmViewModelFactory(context.applicationContext as Application)
    )
    val alarms = mAlarmViewModel.readAllData.observeAsState(listOf()).value
    val mNotiViewModel : NotiViewModel = viewModel(
        factory = NotiViewModelFactory(context.applicationContext as Application)
    )
    val notis = mNotiViewModel.readAllData.observeAsState(listOf()).value
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("AlarmListScreen","[알람목록] Activity 이동성공")
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.mas),
                            contentDescription = "character",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(text = "alarmony",
                            fontWeight = FontWeight.Bold,
                            fontFamily = notosanskr,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                },
                modifier = Modifier.shadow(
                    elevation = 10.dp,
                    ambientColor = Color.Blue,
                    spotColor = Color.Gray,

                    ),
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = "#FFFFFF".toColor()
                ),
                actions = {
                    if (notis.isEmpty()) {
                        IconButton(onClick = { navController.navigate(NavItem.NotiListScreen.route) }) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notification",
                                tint = Color.Black,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    } else {
                        IconButton(onClick = { navController.navigate(NavItem.NotiListScreen.route) }) {
                            Icon(
                                imageVector = Icons.Outlined.NotificationsActive,
                                contentDescription = "Notification_Active",
                                tint = Color.Red,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                    IconButton(onClick = { navController.navigate(NavItem.AccountMtnc.route) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Setting",
                            tint = Color.Black,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val intent = Intent(context,GroupActivity::class.java)
                    launcher.launch(intent)
                },
                shape = CircleShape,
                containerColor = "#00B4D8".toColor(),
                modifier = Modifier
                    .offset(y = -50.dp)
                    .size(60.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Alarm Add",
                    modifier = Modifier
                        .size(40.dp),

                    tint = Color.White

                )
                contentColorFor(backgroundColor = Color.Gray)
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .padding(innerPadding)
            ) {
                val context = LocalContext.current
                val onListItemClick = { text : String ->
                    Toast.makeText(
                        context,
                        text,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ////////////////// 테스트용 버튼
                Button(onClick = { val alarm999 = AlarmDto(
                    999L,
                    "장덕모임",
                    8,
                    45,
                    listOf(true, true, true, true, true, true, true),
                    "자장가",
                    15,
                    true
                )
                    saveTestAlarm(alarm999, context)
                    Toast.makeText(context, "8초 뒤에 알람이 울립니다.", Toast.LENGTH_SHORT).show()}
                ) {
                    Text(text = "8초 뒤 울리는 테스트 알람")
                }
                //////////////////
                LazyColumn{
                    items(alarms.size) {model ->
                        MyListItem(
                            item = alarms[model],
                            onItemClick=onListItemClick,
                            navController = navController
                        )
                    }
                }
            }
        },
        containerColor = "#F9F9F9".toColor()
    )
}

@Composable
fun MyListItem(
    item : Alarm,
    onItemClick: (String) -> Unit,
    navController: NavHostController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(100.dp)
            .shadow(
                elevation = 5.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color.White)
            .fillMaxWidth()
            .clickable { navController.navigate("${NavItem.GroupDetails.route}/${item.alarm_id}") },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = "#FFFFFF".toColor()))

    {
        Column(verticalArrangement = Arrangement.Center) {
            val aTime : Int
            val ampm : String
            if (item.hour >= 0 && item.hour < 12) {
                ampm = "오전"
                if (item.hour == 0) {
                    aTime = 12
                } else {
                    aTime = item.hour
                }
            } else {
                ampm = "오후"
                if (item.hour > 12) {
                    aTime = item.hour - 12
                } else {
                    aTime = item.hour
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 25.dp, top = 5.dp, bottom = 0.dp)) {
                Text(
                    text = ampm,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()) {
                Text(
                    text = if (aTime.toString().length == 1) {"0" + aTime.toString()} else {aTime.toString()}
                            + " : "
                            + if (item.minute.toString().length == 1) {"0" + item.minute.toString()} else {item.minute.toString()},
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 20.dp)
            ) {
                val myDate = item.alarmDate
                if (myDate[0] && myDate[1] && myDate[2] && myDate[3] && myDate[4] && !myDate[5] && !myDate[6]) {
                    Text(text = "주중", modifier = Modifier.padding(start = 5.dp))
                }
                else if (!myDate[0] && !myDate[1] && !myDate[2] && !myDate[3] && !myDate[4] && myDate[5] && myDate[6]) {
                    Text(text = "주말", color= Color.Red, modifier = Modifier.padding(start = 5.dp))
                }
                else if (myDate[0] && myDate[1] && myDate[2] && myDate[3] && myDate[4] && myDate[5] && myDate[6]) {
                    Text(text = "매일", color= Color.Black, modifier = Modifier.padding(start = 5.dp))
                }
                else {
                    if (myDate[0]) {
                        Text(text = "월")
                    } else {
                        Text(text = "월", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[1]) {
                        Text(text = " 화")
                    } else {
                        Text(text = " 화", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[2]) {
                        Text(text = " 수")
                    } else {
                        Text(text = " 수", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[3]) {
                        Text(text = " 목")
                    } else {
                        Text(text = " 목", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[4]) {
                        Text(text = " 금")
                    } else {
                        Text(text = " 금", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[5]) {
                        Text(text = " 토")
                    } else {
                        Text(text = " 토", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[6]) {
                        Text(text = " 일")
                    } else {
                        Text(text = " 일", color= Color.Black.copy(alpha = 0.2f))
                    }

                }
            }
        }
    }
}