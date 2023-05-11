package com.slembers.alarmony.feature.screen

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
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
import com.slembers.alarmony.feature.ui.common.CommonDialog
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.groupDetails.GroupDetailsBoard
import com.slembers.alarmony.feature.ui.groupDetails.GroupDetailsTitle
import com.slembers.alarmony.model.db.Record
import com.slembers.alarmony.network.service.GroupService
import com.slembers.alarmony.viewModel.GroupDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GroupDetailsActivity : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}

@OptIn(ExperimentalCoroutinesApi::class)
@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsScreen(
    navController : NavHostController = rememberNavController(),
    alarmId: Long? = null
) {

    val context = LocalContext.current
    if(alarmId == null) {
        Toast.makeText(context,"에러가 발생했습니다.",Toast.LENGTH_SHORT)
        navController.popBackStack()
    }

    val alarmDao = AlarmDatabase.getInstance(context).alarmDao()
    val isClosed = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(true) }
    val alarm = remember{ mutableStateOf<Alarm>(Alarm(
        alarm_id =  0,
        title =  "임시제목",
        hour =  0,
        minute =  0,
        alarm_date =  listOf(),
        sound_name =  "Nomal",
        sound_volumn =  7,
        vibrate =  true,
    )) }

    var record = remember{ mutableStateOf<Map<String, List<Record>>>(
        hashMapOf(
            "success" to listOf(),
            "failed" to listOf()
        )
    )}

    var details : GroupDetailsViewModel = viewModel(
        factory = GroupDetailsViewModel.GroupViewModelFactory(
            application = context.applicationContext as Application
        )
    )

    LaunchedEffect(Unit) {
        Log.d("alarmDetails","[알람 상세] 초기화 불러오는 중 ...")
        val repository = AlarmRepository(alarmDao)
        val _alarm = withContext(Dispatchers.IO) {
            repository.findAlarm(alarmId!!)
        }

        val _record = details.getRecord(alarmId!!)
        Log.d("alarmDetails","[알람 상세] 초기화 불러오는 완료 ...")
        Log.d("alarmDetails","[알람 상세] alarm : $_alarm")
        alarm.value = _alarm!!
        record.value = _record
    }

    Log.d("alarmDetails","[알람 상세] alarm : $alarm")
    Log.d("alarmDetails","[알람 상세] alarm_date : ${alarm.value.alarm_date}")
    Log.d("alarmDetails","[알람 상세] alarmId : $alarmId")
    Log.d("alarmDetails","[알람 상세] record : $record")
    Log.d("alarmDetails","[알람 상세] details : $details")

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.Group.title,
                navClick = { navController.popBackStack() }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(10.dp)
                    .verticalScroll(
                        state = scrollState,
                        enabled = true
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GroupDetailsTitle(alarm.value)
                GroupDetailsBoard(
                    items = record.value,
                    groupId = alarmId!!
                )
                CardBox(
                    title = { GroupTitle(
                        title = "그룹원 통계",
                        content = { Icon(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .size(30.dp),
                            imageVector = Icons.Filled.BarChart,
                            contentDescription = null
                        )}
                    )}
                )
                CardBox( title = { GroupTitle(
                    title = "그룹 나가기",
                    content = { Icon(
                        modifier = Modifier
                            .padding(end = 10.dp)
                            .size(30.dp),
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = null,
                        tint = Color.Red
                    )},
                    onClick = { isClosed.value = true }
                )}
                )
            }
            if(isClosed.value) {
                CommonDialog(
                    title = "그룹 나가기",
                    context = "정말로 나가겠어요?",
                    isClosed = isClosed,
                    openDialog = openDialog,
                    accept = {
                        CoroutineScope(Dispatchers.IO).launch {
                            GroupService.deleteGroup(alarmId!!)
                            deleteAlarm(alarmId, context)
                        }
                        navController.popBackStack()
                    }
                )
            }
        }
    )
}