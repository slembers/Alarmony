package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.network.repository.MemberService.findId

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun FindId(navController: NavController) {

    var email by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{2,})".toRegex()
    var isEmailError = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "아이디 찾기",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = White),

                )
        },


        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text("가입 시 입력한 이메일 주소로 아이디를 전송합니다.")
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
                    Log.d("확인", "인증번호 보내기")
                    findId(email, context, navController)
                },
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),

                enabled = !isEmailError.value && email.isNotBlank()
            )
            {
                Text(text = "아이디 찾기")
            }
        }
    )
}

/**
 * 이메일
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun FindEmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    isEmailError: MutableState<Boolean>,
) {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{2,})".toRegex()
    TitleText("이메일 *")
    OutlinedTextField(
        value = email,
        onValueChange = {
            onEmailChange(it)
            isEmailError.value = !emailRegex.matches(it)
        },
        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            errorBorderColor = "#EF2B2A".toColor()

        ),
        singleLine = true,
        maxLines = 1,
        isError = isEmailError.value,
        modifier = Modifier
            .fillMaxWidth(),
        keyboardActions = KeyboardActions { },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
    )
    if (email.isNotBlank()) {
        if (isEmailError.value) {
            Log.d("회원", "이메일 정규식 통과못함")
            ErrorMessageText(
                message = "이메일 형식에 맞게 입력해 주세요",
                color = "#EF2B2A".toColor()
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun FindIdTextField(
    username: String,
    onIdChange: (String) -> Unit,
    isIdError: MutableState<Boolean>,
) {
    val usernameRegex = "^[a-z0-9]{5,11}$".toRegex()
    TitleText("아이디 *")
    OutlinedTextField(
        value = username,
        onValueChange = {
            onIdChange(it)
            isIdError.value = !usernameRegex.matches(it)
        },
        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            errorBorderColor = "#EF2B2A".toColor()

        ),
        singleLine = true,
        maxLines = 1,
        isError = isIdError.value,
        modifier = Modifier
            .fillMaxWidth(),
        keyboardActions = KeyboardActions { },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
    )
    if (username.isNotBlank()) {
        if (isIdError.value) {
            ErrorMessageText(
                message = "아이디는 영문, 숫자를 조합하여 4-20자로 입력해주세요.",
                color =  "#EF2B2A".toColor()

            )
        }
    }
}



