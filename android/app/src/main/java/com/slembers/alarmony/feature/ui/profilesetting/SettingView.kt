package com.slembers.alarmony.feature.ui.profilesetting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextButton
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.alarm.deleteAllAlarms
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardDivider
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.notification.deleteAllNotis
import com.slembers.alarmony.feature.screen.MemberActivity
import com.slembers.alarmony.feature.ui.common.AnimationRotation
import com.slembers.alarmony.network.repository.MemberService
import com.slembers.alarmony.util.UriUtil
import com.slembers.alarmony.util.hasWriteExternalStoragePermission
import com.slembers.alarmony.util.requestWriteExternalStoragePermission
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
    val curNickname = remember { mutableStateOf("닉네임") }
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
        isEditMode.value = false
        if(changeName == curNickname.value){
            showToast(context, "현재 닉네임과 같습니다.")
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val result = MemberService.modifyMemberNickname(changeName.trim('"'))
            if (result?.success == true) {
                showToast(context, "닉네임이 변경되었습니다.")
                curNickname.value = changeName
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

    val exitLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("AlarmListScreen", "[알람목록] Activity 이동성공")
        }
    }

    LaunchedEffect(Unit) {
        val myInfo = MemberService.getMyInfo()
        username.value = myInfo?.username.toString()
        curNickname.value = myInfo?.nickname.toString()
        nickname.value = myInfo?.nickname.toString()
        profileImage.value = myInfo?.profileImgUrl.toString()
        email.value = myInfo?.email.toString()
    }

    Column(
        modifier = modifier,
    ) {
        CardBox(
            title = { CardTitle(title = "프로필 설정") },
            content = {
                Column(
                    modifier = Modifier
//                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            top = 0.dp,
                            bottom = 0.dp,
                            end = 20.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CardDivider(color = Color(0xff9A9A9A))

                    /** 이미지 정보 **/
                    Row(
                        modifier = Modifier
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Spacer(modifier = Modifier.size(20.dp))
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(CircleShape)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(
                                            if (profileImage.value.length > 4) {
                                                profileImage.value
                                            } else {
                                                R.drawable.profiledefault
                                            }
                                        )
                                        .build(),
                                    contentDescription = "ImageRequest example",
                                    modifier = Modifier.fillMaxWidth(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "프로필 변경하기",
                                fontSize = 17.sp,
                                style = MaterialTheme.typography.subtitle2.copy(color = Color.Blue),
                                modifier = Modifier
                                    .padding(20.dp)
                                    .clickable {
                                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                            launcher.launch("image/*")
                                        }
                                        else { // 티라미수 미만 버전은 ExternalStorage 접근권한을 유저한테 요청해야함
                                            if (hasWriteExternalStoragePermission(context)) {
                                                launcher.launch("image/*")
                                            } else {
                                                requestWriteExternalStoragePermission()
                                            }
                                        }
                               },
                            )
                        }
                    }
                }
            }
        )

        CardBox(
            title = { CardTitle(title = "계정 정보") },
            content = {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            top = 0.dp,
                            bottom = 0.dp,
                            end = 20.dp
                        ),
                ) {
                    CardDivider(color = Color(0xff9A9A9A))

                    /** 아이디 **/
                    Row(
                        modifier = Modifier
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                Icons.Default.Badge,
                                contentDescription = "ID",
                                Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.size(15.dp))
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text("아이디",)
                            Text(text = username.value,)
                        }
                    }
                    CardDivider(color = "#E9E9E9".toColor())


                    /** 닉네임 변경하기 **/
                    Row(
                        modifier = Modifier
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                Icons.Default.Face,
                                contentDescription = "Nickname",
                                Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.size(15.dp))
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text("닉네임",)
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
                                    modifier = Modifier.padding(5.dp).fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Password,
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
                                    verticalAlignment = Alignment.Bottom,
                                ) {
                                    Text(
                                        text = nickname.value,
                                    )
                                    Text(
                                        text = "    변경하기",
                                        style = MaterialTheme.typography.subtitle2.copy(color = Color.Blue),
                                        modifier = Modifier
                                            .clickable {isEditMode.value = true}
                                    )
                                }
                            }
                        }
                    }
                    CardDivider(color = "#E9E9E9".toColor())


                    /** 이메일 **/
                    Row(
                        modifier = Modifier
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                Icons.Default.Email,
                                contentDescription = "Email",
                                Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.size(15.dp))
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                        Text("이메일",)
                        Text(text = email.value,)
                        }
                    }
                }
            }
        )

        CardBox(
            title = { CardTitle(title = "앱 설정") },
            content = {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            top = 0.dp,
                            bottom = 0.dp,
                            end = 20.dp
                        ),
                ) {
                    CardDivider(color = Color(0xff9A9A9A))

                    /** 어플리케이션 정보 **/
                    Row(
                        modifier = Modifier
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = "App Info",
                                Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.size(15.dp))
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text("어플리케이션 정보",)
//                            Text("Current version : 1.0")
                            Text("Current version : " + getVersionInfo(context))
                        }
                    }
                    CardDivider(color = "#E9E9E9".toColor())


                    /** 푸쉬 알림 설정 **/
                    Row(
                        modifier = Modifier
                            .padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Push Notification",
                                Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.size(15.dp))
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text("푸쉬 알림 설정",)
                            Text("알림 설정 변경하기",
                                style = MaterialTheme.typography.subtitle2.copy(color = Color.Blue),
                                modifier = Modifier
                                    .clickable(onClick = { moveToPushAlertSetting(context) })
                            )
                        }
                    }
                    CardDivider(color = "#E9E9E9".toColor())


                    /** 신고하기 **/
                    Row(
                        modifier = Modifier
                            .padding(5.dp)
                            .clickable(onClick = { navController.navigate(NavItem.ReportPage.route) }),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Icon(
                                Icons.Default.Report,
                                contentDescription = "Report",
                                Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.size(15.dp))
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text("신고하기",)
                            Text("Report inappropriate content")
//                            Text("Current version : " + getVersionInfo(context))
                        }
                    }
                }
            }
        )

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
                    style = MaterialTheme.typography.body1.copy(color = Color.Gray)
                )
            }

            TextButton(
                enabled = !loading.value,
                onClick = {
                    loading.value = true
                    deleteAllAlarms(context)
                    deleteAllNotis(context)
                    CoroutineScope(Dispatchers.IO).async {
                        MemberService.signout()
                        { isSuccess ->
                            if (isSuccess) {
                                Toast.makeText(
                                    context,
                                    "회원탈퇴 성공!",
                                    Toast.LENGTH_LONG
                                ).show()
                                val intent = Intent(context, MemberActivity::class.java)
                                context.startActivity(intent)
                                (context as Activity).finish()
                                exitLauncher.launch(intent)
                            } else {
                                Toast.makeText(
                                    context,
                                    "회원탈퇴 실패...",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        loading.value = false
                    }
                }) {
                Text(
                    text = "회원탈퇴",
                    style = MaterialTheme.typography.body1.copy(color = Color.Gray)
                )
            }
        }
    }
    if (loading.value) {
        AnimationRotation()
    }
}

fun getVersionInfo(context : Context) : String {
    val info: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    return info.versionName
}

fun isNoticeable(context : Context) : Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}

fun moveToPushAlertSetting(context : Context) {
    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    context.startActivity(intent)
}