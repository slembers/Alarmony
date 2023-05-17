package com.slembers.alarmony.feature.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.GroupAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.ui.group.GroupBottomButtom
import com.slembers.alarmony.feature.ui.group.GroupDialog
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.groupInvite.CurrentInvite
import com.slembers.alarmony.feature.ui.groupInvite.SearchInviteMember
import com.slembers.alarmony.network.service.GroupService
import com.slembers.alarmony.viewModel.GroupSearchViewModel
import com.slembers.alarmony.viewModel.GroupViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun InviteScreen(
    navController : NavHostController = rememberNavController(),
    viewModel : GroupViewModel = viewModel()
) {

    val search : GroupSearchViewModel = viewModel()
    val currentMembers = viewModel.members.observeAsState()
    var openDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.GroupInvite.title,
                navClick = { navController.popBackStack() },
                action = {
                    TextButton(onClick = {
                        if(!search.checkedMembers.value!!.isEmpty()) {
                            openDialog = true
                        }
                    }) {
                        Text(
                            text = "완료",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Normal
                            )
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                content = {
                    CurrentInvite(
                        search = search,
                        currentMembers = currentMembers.value ?: mutableListOf()
                    )
                    SearchInviteMember(
                        search = search,
                        currentMembers = currentMembers.value ?: mutableListOf()
                    )
                }
            )
            if(openDialog) {
                AlertDialog(
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.heightIn(
                        min(100.dp,100.dp)
                    ),
                    onDismissRequest = {
                        openDialog = false
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
                                    .padding(top = 10.dp, bottom = 5.dp, start = 5.dp, end = 5.dp)
                                    .clip(shape = RoundedCornerShape(10.dp)),
                                thickness = 2.dp,
                                color = Color.Gray)
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.Gray,
                                text = "그룹에 초대합니다.",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center)
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
                                onClick = { openDialog = false },
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
                                    openDialog = false
                                    val checkedMembers = search.checkedMembers.value
                                    checkedMembers?.map {
                                        it.isNew = true
                                        viewModel.addMember(it)
                                    }
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
                                Text("수락")
                            }
                        }
                    }
                )
            }
        }
    )
}