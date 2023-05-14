package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.network.repository.MemberService
import com.slembers.alarmony.network.repository.MemberService.findPswd

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun Findpswd(navController: NavController) {

    //val context = LocalContext.current
    // var email = remember { mutableStateOf("") }
    var ID = remember { mutableStateOf("") }
    var certnum = remember { mutableStateOf("") }

    var email by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var isEmailError = remember { mutableStateOf(false) }
    var username by rememberSaveable { mutableStateOf("") }
    var isIdError = remember { mutableStateOf(false) }


    /*Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("비밀번호 찾기") },
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp() }) {
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

                mascott(drawing = R.drawable.mascot_foreground)
                logo(drawing = R.drawable.alarmony)





                TextField(

                    value = ID.value,
                    onValueChange = {ID.value = it},
                    label = {Text(text = "아이디")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                TextField(

                    value = email.value,
                    onValueChange = {email.value = it},
                    label = {Text(text = "이메일")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextField(

                    value = certnum.value,
                    onValueChange = {certnum.value = it},
                    label = {Text(text = "인증번호")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Button(
                    onClick = {
                        Log.d("확인", "비밀번호 찾기")
                        findPswd(email.value, ID.value, context,navController)

                    }, modifier = Modifier
                )

                {
                    Text(text = "비밀번호 찾기")
                }
            }
        }

    )*/


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "비밀번호 찾기",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentSize(Alignment.Center),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),

                )
        },


        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text("가입 시 등록하신 아이디와 이메일을 입력하시면, 해당 이메일로 임시번호를 발급합니다.")

                FindIdTextField(
                    username = username,
                    onIdChange = { username = it },
                    isIdError = isIdError
                )

                FindEmailTextField(
                    email = email, // pass the initial email value here
                    onEmailChange = { email = it },
                    isEmailError = isEmailError,
                )
            }
        },
        bottomBar = {

            Button(
                onClick = {
                   findPswd(email, username, context, navController)
                },
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = ((!isEmailError.value && email.isNotBlank()) && (!isIdError.value && username.isNotBlank()))
            )
            {
                Text(text = "비밀번호 찾기")
            }
        }
    )

}

