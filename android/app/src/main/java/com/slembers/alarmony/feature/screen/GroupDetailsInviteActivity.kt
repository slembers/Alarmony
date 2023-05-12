package com.slembers.alarmony.feature.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.ui.group.GroupBottomButtom
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.groupInvite.GroupDefalutProfile
import com.slembers.alarmony.feature.ui.groupInvite.SearchInviteMember
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.network.service.GroupService
import com.slembers.alarmony.viewModel.GroupDetailsViewModel
import com.slembers.alarmony.viewModel.GroupSearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun DetailsInviteScreen(
    navController : NavHostController = rememberNavController(),
    viewModel : GroupDetailsViewModel = viewModel(),
    alarmId : Long? = -1
) {

    val search : GroupSearchViewModel = viewModel()
    var currentMembers = remember { mutableStateOf<MutableList<Member>>(mutableListOf()) }
    val checkMembers = search.checkedMembers.observeAsState()
    // Dialog 관련 상태변수
    val isClosed = remember { mutableStateOf(false)  }
    val openDialog = remember { mutableStateOf(true)  }

    LaunchedEffect(Unit) {
        val _member = viewModel.updateCurrentMember(alarmId!!)
        currentMembers.value = _member
    }


    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.GroupInvite.title,
                navClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            GroupBottomButtom(
                text = "저장",
                onClick = { isClosed.value = true }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                content = {
                    CardBox(
                        title = { CardTitle(title = "초대인원") },
                        content = {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        top = 5.dp,
                                        bottom = 10.dp,
                                        end = 0.dp
                                    ),
                                userScrollEnabled = true
                            ) {

                                items(items = currentMembers.value ) { checked ->
                                    GroupDefalutProfile(
                                        profileImg = checked.profileImg,
                                        nickname = checked.nickname,
                                        newMember = checked.isNew
                                    )
                                }

                                items(items = checkMembers.value ?: listOf()) { checked ->
                                    GroupDefalutProfile(
                                        profileImg = checked.profileImg,
                                        nickname = checked.nickname,
                                        newMember = checked.isNew
                                    )
                                }
                            }
                        }
                    )
                    SearchInviteMember(currentMembers = currentMembers.value )
                }
            )
            if(isClosed.value) {
                if (openDialog.value) {
                    AlertDialog(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.width(320.dp),
                        onDismissRequest = {
                            isClosed.value = false
                        },
                        title = {
                            Column() {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "그룹초대",
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    fontSize = 25.sp
                                )
                                Divider(
                                    modifier = Modifier
                                        .alpha(0.3f)
                                        .padding(
                                            top = 10.dp,
                                            bottom = 5.dp,
                                            start = 5.dp,
                                            end = 5.dp
                                        )
                                        .clip(shape = RoundedCornerShape(10.dp)),
                                    thickness = 2.dp,
                                    color = Color.Gray)
                            }
                        },
                        buttons = {
                            Row(
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth()
                            ) {
                                Button(
                                    onClick = { isClosed.value = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = "#C93636".toColor())
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Close,
                                        contentDescription = "Notification",
                                        tint = Color.White,
                                        modifier = Modifier.size(25.dp)
                                    )
                                    Text(text = "취소")
                                }
                                Button(
                                    onClick = {
                                        openDialog.value = false
                                        val members = checkMembers.value?.map {
                                            it.nickname
                                        }?.toList()
                                        CoroutineScope(Dispatchers.IO).async {
                                            GroupService.groupApi.addMembers(
                                                groupId = alarmId,
                                                members = hashMapOf("members" to (members ?: listOf()))
                                            )
                                        }
                                        Log.i("saved","[그룹초대] ${members}")
                                        navController.popBackStack()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = "#31AF91".toColor()),
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = "Notification",
                                        tint = Color.White,
                                        modifier = Modifier.size(25.dp)
                                    )
                                    Text("저장")
                                }

                            }
                        }
                    )
                }
            }
        }
    )
}