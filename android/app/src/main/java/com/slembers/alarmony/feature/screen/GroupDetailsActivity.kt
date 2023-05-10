package com.slembers.alarmony.feature.screen

import android.os.Bundle
import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.alarm.AlarmDatabase
import com.slembers.alarmony.feature.alarm.AlarmRepository
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.groupDetails.GroupDetailsBoard
import com.slembers.alarmony.feature.ui.groupDetails.GroupDetailsTitle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class GroupDetailsActivity : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsScreen(
    navController : NavHostController = rememberNavController(),
    alarmId: Long? = null
) {


    val context = LocalContext.current
    val alarmDao = AlarmDatabase.getInstance(context).alarmDao()
    val repositroy = AlarmRepository(alarmDao)
    if(alarmId == null) navController.popBackStack()
    val alarm by lazy {
        CoroutineScope(Dispatchers.IO).async {
            repositroy.findAlarm(alarmId!!)
        }
    }
    Log.d("alarmDetails","[알람 상세] alarm : $alarm")
    Log.d("alarmDetails","[알람 상세] alarmId : $alarmId")
    // 알람 기본키가 null 이면 뒤로가기
//    if(alarmId == null) navController.popBackStack()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.Group.title,
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
                GroupDetailsTitle(alarm.getCompleted())
                GroupDetailsBoard()
                CardBox(
                    title = { GroupTitle(
                        title = "그룹원 통계",
                        content = { Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = Icons.Filled.BarChart,
                            contentDescription = null
                        )}
                    )}
                )
                CardBox( title = { GroupTitle(
                    title = "그룹 나가기",
                    content = { Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = null,
                        tint = Color.Red
                    )}
                )}
                )
            }
        }
    )
}