package com.slembers.alarmony.feature.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material.icons.outlined.GroupRemove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.alarm.Alarm
import com.slembers.alarmony.feature.alarm.AlarmDatabase
import com.slembers.alarmony.feature.alarm.AlarmRepository
import com.slembers.alarmony.feature.alarm.deleteAlarm
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.ui.common.AnimationRotation
import com.slembers.alarmony.feature.ui.common.CommonDialog
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.groupDetails.GroupDetailsBoard
import com.slembers.alarmony.feature.ui.groupDetails.GroupDetailsTitle
import com.slembers.alarmony.model.db.Record
import com.slembers.alarmony.network.service.GroupService
import com.slembers.alarmony.util.showToast
import com.slembers.alarmony.viewModel.GroupDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsScreen(
    navController : NavHostController = rememberNavController(),
    details : GroupDetailsViewModel = viewModel(),
    alarmId: Long? = null
) {

    val context = LocalContext.current
    if(alarmId == null) {
        Toast.makeText(context,"에러가 발생했습니다.",Toast.LENGTH_SHORT)
        navController.popBackStack()
    }

    val alarmDao = AlarmDatabase.getInstance(context).alarmDao()
    var loading by remember { mutableStateOf(false) }
    val isClosed = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(true) }
    val alarm = remember{ mutableStateOf<Alarm>(Alarm(
        alarmId =  0,
        title =  "",
        hour =  0,
        minute =  0,
        alarmDate =  listOf(),
        soundName =  "Nomal",
        soundVolume =  7,
        vibrate =  true,
        host = false,
        content = "임시내용"
    )) }

    var record = remember{ mutableStateOf<Map<String, List<Record>>>(
        hashMapOf(
            "success" to listOf(),
            "failed" to listOf()
        )
    )}

    var memberCnt by details.memberCnt

    LaunchedEffect(Unit) {
        Log.d("alarmDetails","[알람 상세] 초기화 불러오는 중 ...")
        val repository = AlarmRepository(alarmDao)
        val _alarm = withContext(Dispatchers.IO) {
            repository.findAlarm(alarmId!!)
        }

        val _record = details.getRecord(alarmId!!)
        Log.d("alarmDetails","[알람 상세] 초기화 불러오는 완료 ...")
        Log.d("alarmDetails","[알람 상세] alarm : $_alarm")
        Log.d("alarmDetails","[알람 상세] alarm : $memberCnt")

        alarm.value = _alarm!!
        record.value = _record
    }

    Log.d("alarmDetails","[알람 상세] alarm : $alarm")
    Log.d("alarmDetails","[알람 상세] alarm_date : ${alarm.value.alarmDate}")
    Log.d("alarmDetails","[알람 상세] alarmId : $alarmId")
    Log.d("alarmDetails","[알람 상세] record : $record")
    Log.d("alarmDetails","[알람 상세] details : $details")

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            GroupToolBar(
                //title = NavItem.GroupDetails.title, 수정[1]
                title = alarm.value.title,
                navClick = { navController.popBackStack() },
                action = {
                    if(alarm.value.host) {
                        Row(
                            modifier = Modifier.wrapContentWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if(memberCnt!! >= 2) {
//                                IconButton(onClick = { navController.navigate(NavItem.GroupDetailsInvite.route + "/$alarmId") }) {
//                                    Icon(
//                                        imageVector = Icons.Outlined.GroupRemove,
//                                        contentDescription = "groupeXile",
//                                        tint = Color.Red,
//                                        modifier = Modifier.size(25.dp)
//                                    )
//                                }
                            }
                            IconButton(onClick = { navController.navigate(NavItem.GroupDetailsInvite.route + "/$alarmId") }) {
                                Icon(
                                    imageVector = Icons.Outlined.GroupAdd,
                                    contentDescription = "groupAdd",
                                    tint = Color.Black,
                                    modifier = Modifier.size(25.dp)
                                )
                            }
                        }

                    }
                }
            )
        },
        containerColor = "#F9F9F9".toColor(),
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(10.dp)
                    .fillMaxHeight()
                    .verticalScroll(
                        state = scrollState,
                        enabled = true
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GroupDetailsTitle(alarm.value)
                GroupDetailsBoard(
                    items = record.value,
                    groupId = alarmId!!,
                    host = alarm.value.host && currentDay(alarm.value)
                )
                CardBox(
                    title = { GroupTitle(
                        title = "그룹원 통계",
                        content = { Icon(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(15.dp),
                            imageVector = Icons.Filled.BarChart,
                            contentDescription = null
                        )}
                    )}
                )
                if(!alarm.value.host) {
                    CardBox(title = {
                        GroupTitle(
                            title = "그룹 나가기",
                            content = {
                                Icon(
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .size(15.dp),
                                    imageVector = Icons.Filled.ExitToApp,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            },
                            enable = !loading,
                            onClick = {
//                        if(alarm.value.host) {
//                            val memberCnt = record.value["success"]?.size?.plus(record.value["failed"]?.size!!)
//                            Log.d("alarmDetails","[알람 상세] 알람 인원 수 : $memberCnt")
//                            if(memberCnt != 1) {
//                                showToast(context,"그룹장은 혼자일때만 나갈 수 있습니다.")
//                                return@GroupTitle
//                            }
//                        }
                                isClosed.value = true
                            }
                        )
                    })
                } else if(alarm.value.host && memberCnt!! <= 1) {
                    CardBox(title = {
                        GroupTitle(
                            title = "그룹 나가기",
                            content = {
                                Icon(
                                    modifier = Modifier
                                        .padding(end = 10.dp)
                                        .size(15.dp),
                                    imageVector = Icons.Filled.ExitToApp,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            },
                            enable = !loading,
                            onClick = { isClosed.value = true }
                        )
                    })
                }
            }
            if(isClosed.value) {
                CommonDialog(
                    title = "그룹 나가기",
                    context = "정말로 나가겠어요?",
                    isClosed = isClosed,
                    openDialog = openDialog,
                    accept = {
                        loading = true
                        isClosed.value = false
                        CoroutineScope(Dispatchers.IO).async {
                            GroupService.deleteGroup(alarmId!!)
                            deleteAlarm(alarmId, context)
                        }
                        loading = false
                        navController.popBackStack()
                    }
                )
            }
        }
    )
    if(loading) {
        AnimationRotation()
    }
}

private fun currentDay(alarm : Alarm?) : Boolean {

    val hour = alarm!!.hour
    val minute = alarm!!.minute
    val day = alarm!!.alarmDate

    Log.d("","현재시간 : ${alarm.hour} : ${alarm.minute}")
    Log.d("","현재주간 : ${alarm.alarmDate}")
    
    val local = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
    Log.d("","현재시간 : $local")
    val hf = local.format(DateTimeFormatter.ofPattern("HH")).toInt()
    val mf = local.format(DateTimeFormatter.ofPattern("mm")).toInt()
    val df = local.format(DateTimeFormatter.ofPattern("E"))

    Log.d("","현재시간 : $hf : $mf : $df")

    // 현재날짜와 배열의 인덱스가 false면 불허
    when(df) {
        "월" -> if(!day[0]) return false
        "화" -> if(!day[1]) return false
        "수" -> if(!day[2]) return false
        "목" -> if(!day[3]) return false
        "금" -> if(!day[4]) return false
        "토" -> if(!day[5]) return false
        "일" -> if(!day[6]) return false
    }
    
    // 시간이 작으면 알람보내기 불용
    if( hf > hour ) {
        return true
    } else {
        // 시간이 같다면 분이 넘어야 허용
        if( hf == hour ) {
            return mf >= minute
        } else { // 시간이 넘으면 허용
            return false
        }
    }
}