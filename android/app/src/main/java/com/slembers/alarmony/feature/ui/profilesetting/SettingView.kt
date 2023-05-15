package com.slembers.alarmony.feature.ui.profilesetting
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.alarm.deleteAllAlarms
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.notification.deleteAllNotis
import com.slembers.alarmony.feature.screen.MemberActivity
import com.slembers.alarmony.feature.ui.common.AnimationRotation
import com.slembers.alarmony.network.repository.MemberService
import com.slembers.alarmony.util.UriUtil
import com.slembers.alarmony.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Preview
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalGlideComposeApi::class,
    ExperimentalMaterialApi::class
)
//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingView(
    navController: NavController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    // 이메일과 닉네임 정보를 가지고 있는 상태 변수
    val username = remember { mutableStateOf("아이디") }
    val nickname = remember { mutableStateOf("닉네임") }
    val email = remember { mutableStateOf("xxxx@naver.com") }
    // 프로필 이미지를 가지고 있는 상태 변수
    val profileImage = remember { mutableStateOf("") }
    // 닉네임 수정 모드를 제어하는 상태 변수
    val isEditMode = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val mySelectedUri = remember { mutableStateOf<Uri>(Uri.EMPTY) }
    // 로딩화면을 보여주는 변수
    val loading = remember { mutableStateOf(false) }
    val pushAlert = remember {
        mutableStateOf(true)
    }

    fun changeNickname(changeName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            isEditMode.value = !isEditMode.value
            val result = MemberService.modifyMemberNickname(changeName.trim('"'))
            if (result?.success == true) {
                showToast(context, "닉네임이 변경되었습니다.")
            } else {
                showToast(context, "닉네임이 이미 사용중입니다.")
                nickname.value = result?.nickname.toString()
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                mySelectedUri.value = uri
                CoroutineScope(Dispatchers.IO).launch {
                    val result = MemberService.modifyMemberImage(
                        imgProfileFile = UriUtil.uriToMultiPart(
                            mySelectedUri.value,
                            context
                        )
                    )
                    if (result != null) {
                        profileImage.value = result
                    }
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        val myInfo = MemberService.getMyInfo()
        username.value = myInfo?.username.toString()
        nickname.value = myInfo?.nickname.toString()
        profileImage.value = myInfo?.profileImgUrl.toString()
        email.value = myInfo?.email.toString()
    }

    Column(
        modifier = modifier,
    ) {
        ListItem(
            leadingContent = {
                ProfileImageView(profileImage = profileImage.value)
            },
            headlineContent = {
                Text(
                    text = username.value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
//                        modifier = Modifier.padding(top = 25.dp)
                )
            },
            supportingContent = {
                Text(
                    text = "Change your profile Image",
                    fontSize = 13.sp,
                    style = MaterialTheme.typography.subtitle2.copy(color = Color.Blue),
                    modifier = Modifier
                        .clickable { launcher.launch("image/*") },
                )
            },
            modifier = Modifier.padding(16.dp),
            colors = ListItemDefaults.colors("#F9F9F9".toColor())
        )

        Divider()

        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email",
                    Modifier.size(40.dp)
                )
            },
            headlineContent = { Text("이메일") },
            supportingContent = {
                Text(
                    text = email.value,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier,
            colors = ListItemDefaults.colors("#F9F9F9".toColor())
        )

        Divider()

        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Nickname",
                    Modifier.size(40.dp)
                )
            },
            headlineContent = { Text("닉네임 변경하기") },
            supportingContent = {
                // 닉네임
                if (isEditMode.value) {
                    // 수정 모드일 경우 TextField를 보여준다.
                    OutlinedTextField(
                        value = nickname.value,
                        onValueChange = {
                            nickname.value = it
                            if (it.endsWith("\n")) {
                                nickname.value = nickname.value.trim()
                                changeNickname(nickname.value)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                changeNickname(nickname.value)
                            }
                        )

                    )
                } else {
                    // 수정 모드가 아닐 경우 Text를 보여준다.
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        androidx.compose.material.Text(
                            text = "current : ${nickname.value}",
//                                style = MaterialTheme.typography.subtitle2.copy(color = Color.Blue),
                            modifier = Modifier.clickable {
                                isEditMode.value = !isEditMode.value
                                if (!isEditMode.value) {
                                    Log.d(
                                        "upload",
                                        mySelectedUri.value.toString()
                                    )
                                    // 수정 모드를 끝낼 때 서버에 닉네임 변경 요청을 보내는 로직
                                }
                            }
                        )
                    }
                }
            },
            modifier = Modifier
                .clickable(onClick = { isEditMode.value = !isEditMode.value }),
            colors = ListItemDefaults.colors("#F9F9F9".toColor())
        )

        Divider()

        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.ColorLens,
                    contentDescription = "Theme",
                    Modifier.size(40.dp)
                )
            },
            headlineContent = { Text("테마 설정") },
            supportingContent = { Text("Change the app theme.") },
            modifier = Modifier
                .clickable(onClick = {/* TODO */ }),
            colors = ListItemDefaults.colors("#F9F9F9".toColor())
        )

        Divider()

        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "App Info",
                    Modifier.size(40.dp)
                )
            },
            headlineContent = { Text("어플리케이션 정보") },
            supportingContent = { Text("View app version and build number") },
            modifier = Modifier
                .clickable(onClick = {/* TODO */ }),
            colors = ListItemDefaults.colors("#F9F9F9".toColor())
        )

        Divider()

        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Push Notifications",
                    Modifier.size(40.dp)
                )
            },
            headlineContent = { Text("푸쉬 알림 설정") },
            supportingContent = { Text("current : " + if(pushAlert.value) "ON" else "OFF" ) },
            modifier = Modifier
                .clickable(onClick = {pushAlert.value = !pushAlert.value}),
            colors = ListItemDefaults.colors("#F9F9F9".toColor())
        )

        Divider()

        ListItem(
            leadingContent = {
                Icon(
                    Icons.Default.Flag,
                    contentDescription = "Report",
                    Modifier.size(40.dp)
                )
            },
            headlineContent = { Text("신고하기") },
            supportingContent = { Text("Report inappropriate content") },
            modifier = Modifier
                .clickable(onClick = { navController.navigate(NavItem.ReportPage.route) }),
            colors = ListItemDefaults.colors("#F9F9F9".toColor())
        )

        Divider()

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            TextButton(
                enabled = !loading.value,
                onClick = {
                    loading.value = true
                    CoroutineScope(Dispatchers.IO).async {
                        deleteAllAlarms(context)
                        deleteAllNotis(context)
                        val result = MemberService.logOut()
                        if (result) {
                            val intent = Intent(context, MemberActivity::class.java)
                            context.startActivity(intent)
                            (context as Activity).finish()
                            loading.value = false
                        }
                        loading.value = false
                    }
                }
            ) {
                Text(
                    text = "로그아웃 |",
                    style = MaterialTheme.typography.body1.copy(color = Color.Blue)
                )
            }

            TextButton(onClick = { /* 아이디 찾기 버튼 클릭 시 처리할 동작 */ }) {
                Text(
                    text = "회원탈퇴",
                    style = MaterialTheme.typography.body1.copy(color = Color.Red)
                )
            }
        }
    }
    if (loading.value) {
        AnimationRotation()
    }
}
