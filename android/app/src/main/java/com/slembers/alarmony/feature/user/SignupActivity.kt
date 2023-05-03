package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.interaction.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.unit.dp
import com.slembers.alarmony.network.repository.MemberService.singup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI.navigateUp


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignupScreen(navController: NavController) {
//    이번엔 state가 아니라 String형식으로 저장해보기
    var ID  = remember { mutableStateOf("") }
    var password = remember { mutableStateOf("") }
    var passwordConfirm = remember { mutableStateOf("") }
    var nickname = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var passwordVisibility1 by remember { mutableStateOf(false) }
    var passwordVisibility2 by remember { mutableStateOf(false) }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("회원가입") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = ID.value,
                    onValueChange = { ID.value = it },
                    label = { Text(text = "아이디") },
//                    keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
////                .focusRequester(focusRequester)
//                        .onFocusEvent {
//                            ID.value = ""
//                        }
                    ,
                    singleLine = true,
                    maxLines = 1,
//            onFocusChange = { focusState: FocusState ->
//                if (focusState.isFocused) {
//                    ID.value = ""
//                } else {
//                    if (ID.value.isEmpty()) {
//                        ID.value = "영문, 숫자 5-11자"
//                    }
//                }
//            }
                )


                TextField(

                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text(text = "비밀번호") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,
//            보안상 이유로 가리기
//            visualTransformation = PasswordVisualTransformation(),
//            아이콘 클리글 통해 비밀번호 보이게하기(클릭하고 있는 동안 보이게 하면 더 멋진 UI가 될듯!)
//            아이콘은 tailingIcon사용
                    visualTransformation = if (passwordVisibility1) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility1 = !passwordVisibility1 }) {
                            Icon(
                                if (passwordVisibility1) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisibility1) "비밀번호 가리기" else "비밀번호 보이기"
                            )
                        }
                    }

                    //            focusRequester = focusRequester
                )
                TextField(
                    value = passwordConfirm.value,
                    onValueChange = { passwordConfirm.value = it },
                    label = { Text(text = "비밀번호 재입력") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
//            포커스가 바뀌면 입력창 안보이게 하기!
                    maxLines = 1,
//            visualTransformation = PasswordVisualTransformation(),
                    visualTransformation = if (passwordVisibility2) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility2 = !passwordVisibility2 }) {
                            Icon(
                                if (passwordVisibility2) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisibility2) "비밀번호 가리기" else "비밀번호 보이기"
                            )
                        }
                    },


                    )

                TextField(
                    value = email.value,
                    onValueChange = {email.value = it},
                    label = {Text(text = "이메일")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,

                    )
                Text(
                    text = "계정 분실 시 본인인증 정보로 활용됩니다.",
                    modifier = Modifier.fillMaxWidth()

                )
                TextField(
                    value = nickname.value,
                    onValueChange = {nickname.value = it},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),

                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {Text(text = "닉네임")},
                    singleLine = true,
                    maxLines = 1,



                    )









                Button(
                    onClick = {
                        // TODO: 회원가입 로직 구현
                        // 회원가입이 완료되면 Snackbar 띄우기
                        Log.d("회원", "회원가입버튼누름")
                        singup(ID.value, password.value, nickname.value, email.value)

                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "본인인증하고 가입하기")
                }
            }
        }
    )

}