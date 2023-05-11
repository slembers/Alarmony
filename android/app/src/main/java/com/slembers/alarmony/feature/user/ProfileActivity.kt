package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.network.repository.MemberService.logOut
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileSetting(navController: NavController) {

    // 이메일과 닉네임 정보를 가지고 있는 상태 변수
    var nickname = remember { mutableStateOf("닉네임") }
    var email = remember { mutableStateOf("xxxx@naver.com") }

    // 프로필 이미지를 가지고 있는 상태 변수
    var profileImage = remember { mutableStateOf(R.drawable.mascot_foreground) }

    // 닉네임 수정 모드를 제어하는 상태 변수
    var isEditMode = remember { mutableStateOf(false) }
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("계정설정") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp()}) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 프로필 이미지
                Image(
                    painter = painterResource(profileImage.value),
                    contentDescription = "프로필 이미지",
                    modifier = Modifier
                        .size(100.dp)
                        .clickable(onClick = { /* 프로필 사진 변경 로직 */ })
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 이메일
                Text(
                    text = email.value,
                    style = MaterialTheme.typography.subtitle1,


                )

                Spacer(modifier = Modifier.height(16.dp))

                // 닉네임
                if (isEditMode.value) {
                    // 수정 모드일 경우 TextField를 보여준다.
                    OutlinedTextField(
                        value = nickname.value,
                        onValueChange = { nickname.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("닉네임") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),

                    )
                } else {
                    // 수정 모드가 아닐 경우 Text를 보여준다.
                    Text(
                        text = nickname.value,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 닉네임 수정 버튼
                Button(
                    onClick = {
                        isEditMode.value = !isEditMode.value
                        if (!isEditMode.value) {
                            // 수정 모드를 끝낼 때 서버에 닉네임 변경 요청을 보내는 로직
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (isEditMode.value) "완료" else "수정",
                        style = MaterialTheme.typography.button
                    )
                }
                Row(
                    modifier = Modifier
//            .fillMaxWidth(),
                    ,

                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = {
                        logOut(context, navController)})


                    {
                        Text(text = "로그아웃 |")
                    }

                    TextButton(onClick = { /* 아이디 찾기 버튼 클릭 시 처리할 동작 */ }) {
                        Text(text = "회원탈퇴",
                            style = MaterialTheme.typography.body1.copy(color = Color.Red)
                        )
                    }

                }
            }

        }

    )

}