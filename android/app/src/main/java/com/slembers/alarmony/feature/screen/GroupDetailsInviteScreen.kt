package com.slembers.alarmony.feature.screen

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
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
import com.slembers.alarmony.feature.ui.groupInvite.SearchMember
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
    val context = LocalContext.current
    val search : GroupSearchViewModel = viewModel()
    val searchMembers = search.searchMembers.observeAsState()
    var currentMembers by remember { mutableStateOf<MutableList<Member>>(mutableListOf()) }
    val checkMembers = search.checkedMembers.observeAsState()
    // 검색 단어 저장 변수
    var text by remember { mutableStateOf("") }
    // Dialog 관련 상태변수
    var isClosed by remember { mutableStateOf(false)  }
    var openDialog by remember { mutableStateOf(true)  }
    val keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
        //    capitalization = KeyboardCapitalization.Sentences
    )

    LaunchedEffect(Unit) {
        val _member = viewModel.updateCurrentMember(alarmId!!)
        currentMembers = _member
    }
    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.GroupInvite.title,
                navClick = { navController.popBackStack() },
                action = {
                    TextButton(onClick = { isClosed = true }) {
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
        containerColor = "#F9F9F9".toColor(),
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
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
                                    )
                                    .heightIn( min(50.dp, 50.dp) ),
                                userScrollEnabled = true
                            ) {

                                items(items = currentMembers ) { checked ->
                                    GroupDefalutProfile(
                                        member = checked,
                                    )
                                }
                                items(items = checkMembers.value ?: listOf()) { checked ->
                                    GroupDefalutProfile(
                                        member = checked,
                                        cancel = { search.removeCheckedMember(it) }
                                    )
                                }
                            }
                        }
                    )
                    CardBox(
                        title = { CardTitle(title = "검색") },
                        content = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        start = 20.dp,
                                        top = 10.dp,
                                        bottom = 10.dp,
                                        end = 20.dp
                                    )
                            ) {
                                BasicTextField(
                                    value = text,
                                    onValueChange = {
                                        text = it
                                        search.searchApi(keyword = text)
                                    },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        color = Color.Black,
                                        fontSize = 14.sp,
                                       // fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Normal,
                                    ),
                                    modifier = Modifier
                                        .height(40.dp)
                                        .fillMaxWidth(),
                                    keyboardOptions = keyboardOptions,
                                    decorationBox = { innerTextField ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(
                                                    Color(0xffD9D9D9),
                                                    shape = MaterialTheme.shapes.extraLarge
                                                )
                                                .padding(horizontal = 16.dp), // inner padding
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Favorite icon",
                                                tint = Color.DarkGray
                                            )
                                          //  Spacer(modifier = Modifier.width(width = 8.dp))

                                            Box(
                                                modifier = Modifier
                                                    .padding(start = 10.dp) // margin left and right
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 10.dp), // inner padding
                                            ) {
                                            if (text.isEmpty()) {
                                                Text(
                                                    text = "닉네임을 입력해주세요.",
                                                    modifier = Modifier.fillMaxWidth(1f),
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = Color.LightGray
                                                )
                                            }
                                            innerTextField()
                                        }
                                        }
                                    }
                                )

                                LazyColumn() {
                                    items(searchMembers.value ?: mutableListOf()) {
                                        val member = Member(
                                            nickname = it.nickname,
                                            profileImg = it.profileImg,
                                            isNew = true
                                        )
                                        // 현재 인원에 포함되면 안됨
                                        if(!currentMembers.contains(member) && !checkMembers.value!!.contains(member)) {
                                            SearchMember(
                                                member = it,
                                                onCheckedChange = {
                                                    if (checkMembers.value!!.contains(it))
                                                        search.removeCheckedMember(it)
                                                    else
                                                        search.addCheckedMember(it)
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                }
            )
            if(isClosed) {
                if (openDialog) {
                    AlertDialog(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.width(320.dp),
                        onDismissRequest = {
                            isClosed = false
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
                                    onClick = { isClosed = false },
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
                                        openDialog = false // 팝업창 닫기
                                        val members = checkMembers.value?.map { it.nickname }?.toList()
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

