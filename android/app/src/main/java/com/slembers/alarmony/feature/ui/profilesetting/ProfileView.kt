package com.slembers.alarmony.feature.ui.profilesetting

import android.annotation.SuppressLint
import android.net.Uri
import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.screen.MemberActivity
import com.slembers.alarmony.network.repository.MemberService
import com.slembers.alarmony.feature.ui.common.AnimationRotation
import com.slembers.alarmony.feature.ui.common.ImageUploader
import com.slembers.alarmony.network.repository.MemberService.logOut
import com.slembers.alarmony.util.UriUtil.uriToMultiPart
import com.slembers.alarmony.util.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.async

@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileView(navController: NavController = rememberNavController()) {
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


    LaunchedEffect(Unit) {
        val myInfo = MemberService.getMyInfo()
        username.value = myInfo?.username.toString()
        nickname.value = myInfo?.nickname.toString()
        profileImage.value = myInfo?.profileImgUrl.toString()
        email.value = myInfo?.email.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row {

            Column(modifier = Modifier.padding(16.dp))
            {
                // 프로필 이미지
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileImageView(profileImage = profileImage.value)

                        // 이미지 수정 버튼
                        ImageUploader(onImageUploadComplete = { uri ->
                            Log.d("image", "이미지 선택 완료")
                            mySelectedUri.value = uri

                            CoroutineScope(Dispatchers.IO).launch {
                                val result = MemberService.modifyMemberImage(
                                    imgProfileFile = uriToMultiPart(
                                        mySelectedUri.value,
                                        context
                                    )
                                )
                                if (result != null) {
                                    profileImage.value = result
                                }
                            }
                        })
                    }
                    Column(
                        Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        // 아이디
                        Text(
                            text = username.value,
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Normal
                            )
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // 이메일
                        Text(
                            text = email.value,
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 17.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Normal
                            )

                        )
                        Spacer(modifier = Modifier.height(16.dp))
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
                                label = { Text("닉네임") },
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
                                Text(
                                    text = nickname.value,
                                    style = MaterialTheme.typography.h6,
                                )
                                Text(
                                    text = "  (닉네임 변경)",
                                    style = MaterialTheme.typography.subtitle2.copy(color = Color.Blue),
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
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TextButton(
                enabled = !loading.value,
                onClick = {
                    loading.value = true
                    CoroutineScope(Dispatchers.IO).async {
                        val result = logOut()
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
                Text(text = "로그아웃 |")
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