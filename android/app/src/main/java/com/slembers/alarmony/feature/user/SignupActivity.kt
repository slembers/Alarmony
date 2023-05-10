package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI.navigateUp
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.ui.common.ShowAlertDialog
import com.slembers.alarmony.model.db.SignupRequest
import com.slembers.alarmony.network.repository.MemberService.checkEmail
import com.slembers.alarmony.network.repository.MemberService.checkId
import com.slembers.alarmony.network.repository.MemberService.checkNickname
import kotlinx.coroutines.runBlocking


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SignupScreen(navController: NavController) {
//    이번엔 state가 아니라 String형식으로 저장해보기
    var username  by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var passwordVisibility1 by remember { mutableStateOf(false) }
    var passwordVisibility2 by remember { mutableStateOf(false) }
    val context = LocalContext.current



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
                    value = username,
                    onValueChange = {
                        username = it
                        checkId(it)
                                    },
//                    onValueChange가 될때마다 로그인 중복체크 함수checkID(username)가 작동되도록 수정


                    label = { Text(text = "아이디") },
//                    keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),

                    modifier = Modifier
                        .fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,

                )


                TextField(

                    value = password,
                    onValueChange = {
                        password = it

                                    },
                    label = { Text(text = "비밀번호") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,
                    visualTransformation = if (passwordVisibility1) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility1 = !passwordVisibility1 }) {
                            Icon(
                                if (passwordVisibility1) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisibility1) "비밀번호 가리기" else "비밀번호 보이기"
                            )
                        }
                    }
                )
                TextField(
                    value = passwordConfirm,
                    onValueChange = { passwordConfirm = it },
                    label = { Text(text = "비밀번호 재입력") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,
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
                    value = email,
                    onValueChange = {email = it
                        checkEmail(it)
                                    },
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
                    value = nickname,
                    onValueChange = {nickname = it
                        checkNickname(it)
                    },
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
                        var result = false
                        // 회원가입이 완료되면 Snackbar 띄우기
                        Log.d("회원", "회원가입버튼누름")

                            singup(SignupRequest(
                                username = username,
                                password = password,
                                nickname = nickname,
                                email = email
                            )) { isSuccess ->
                                result = isSuccess
                                Log.d("test", "${result}")

                                if (result == true) {
                                    Log.d("response", "${result}")
//                            ShowAlertDialog(
//                                true,
//                                "알림",
//                                "회원가입에 성공했습니다.",
//                                context,
//                                {}
//                            )
                                    Toast.makeText(
                                        context,
                                        "회원가입 완료! 이메일을 확인해주세요.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate(NavItem.LoginScreen.route)

                                } else {
                                    Log.d("response", "${result}")
                                    Toast.makeText(
                                        context,
                                        "회원가입 실패...",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

//                        ShowAlertDialog(true, "알림", "회원가입이 성공", {})
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "본인인증하고 가입하기")
                }
            }
        }
    )

}