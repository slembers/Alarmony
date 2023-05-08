package com.slembers.alarmony.feature.ui.group

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.viewModel.GroupSearchViewModel
import com.slembers.alarmony.viewModel.GroupViewModel
import kotlin.streams.toList


@ExperimentalGlideComposeApi
@Preview
@Composable
@ExperimentalMaterial3Api
fun GroupDialog(
    isClicked : MutableState<Boolean> = mutableStateOf(false),
    navController : NavHostController = rememberNavController(),
    group : GroupViewModel = viewModel(),
    search: GroupSearchViewModel = viewModel()
) {

    val checkedMembers = search.checkedMembers.observeAsState()
    val isExist : List<Member> = group.members.value?.map {
        Member(it.nickname, it.profileImg, false)
    }?.toList() ?: listOf()
    val openDialog = remember { mutableStateOf(true)  }

    if (openDialog.value) {
        AlertDialog(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.width(320.dp),
            onDismissRequest = {
                openDialog.value = false
                isClicked.value = false
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
                        onClick = { isClicked.value = false },
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
                            checkedMembers.value?.map {
                                if(!isExist.contains(it))
                                    group.addMember(
                                        MemberDto(nickname = it.nickname, profileImg = it.profileImg)
                                )
                            }
                            Log.i("saved","[그룹초대] ${group.members}")
                            search.clearCheckedMember()
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