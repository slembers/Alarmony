package com.slembers.alarmony.feature.user

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.focus.onFocusChanged



@Composable
fun SignupScreen() {
//    이번엔 state가 아니라 String형식으로 저장해보기
    var ID  = remember { mutableStateOf("영문, 숫자 5-11자") }
    var password = remember { mutableStateOf("숫자, 영문, 특수문자 조합 최소 8자") }
    var passwordConfirm = remember { mutableStateOf("비밀번호 재입력") }
//    var nickname = remember { mutableStateOf("영문, 숫자 5-11자") }
    var email = remember { mutableStateOf("") }


//    val focusRequester = remember { FocusRequester() }

    Column(

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = ID.value,
            onValueChange = { ID.value = it },
//            label = "아이디",
            label = { Text(text = "비밀번호") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
////                .focusRequester(focusRequester)
//                .onFocusChanged {
//                    ID.value = ""
//                }
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
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
//            보안상 이유로 가리기
            visualTransformation = PasswordVisualTransformation(),
            //            focusRequester = focusRequester
        )
        TextField(
            value = passwordConfirm.value,
            onValueChange = { passwordConfirm.value = it },
            label = { Text(text = "비밀번호 재입력") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation(),
        )

        TextField(
            value = email.value,
            onValueChange = {email.value = it},
            label = {Text(text = "이메일")},
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,

            )
        Text(
            text = "계정 분실 시 본인인증 정보로 활용됩니다.",
            modifier = Modifier.fillMaxWidth()

        )








        Button(
            onClick = {
                // TODO: 회원가입 로직 구현
                // 회원가입이 완료되면 Snackbar 띄우기
                Log.d("회원", "회원가입버튼누름")

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "회원가입")
        }
    }
}