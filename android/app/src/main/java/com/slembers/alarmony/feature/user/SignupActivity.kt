package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
//import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI.navigateUp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.ui.common.ShowAlertDialog
import com.slembers.alarmony.model.db.SignupRequest
import com.slembers.alarmony.network.repository.MemberService.checkEmail
import com.slembers.alarmony.network.repository.MemberService.checkId
import com.slembers.alarmony.network.repository.MemberService.checkNickname
import kotlinx.coroutines.runBlocking

import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Green


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
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
    var usernameResult = false
    var emailResult = false
    var nicknameResult = false
    var duplicatedUsername by remember {mutableStateOf(false)}
    var duplicatedEmail by remember {mutableStateOf(false)}
    var duplicatedNickname by remember {mutableStateOf(false)}
    val usernameregex = "^[a-z0-9]{4,20}$".toRegex()
    val passwordregex = "^[a-zA-Z\\d]{8,16}\$".toRegex()
    val nicknameregex = "^[가-힣a-zA-Z0-9]{2,10}\$".toRegex()
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$".toRegex()
    var usernameFocused by remember {mutableStateOf(false)}
    var emailFocused by remember {mutableStateOf(false)}
    var nicknameFocused by remember {mutableStateOf(false)}

    val focusRequester = remember { FocusRequester() }



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
//                    정규표현식을 통과할 경우에만 아래 api통신인 checkId(username)이 기능하도록 작성
//                    그리고 해당TextField에 포커스가 존재할만 checkId(username) 작동
                    onValueChange = { username = it },

                    label = { Text(text = "아이디") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged { usernameFocused = if (it.isFocused) true else false }
                        .fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,

                )
                if (!usernameregex.matches(username)) {

                    Log.d("response", "유저네임 포커스 ${usernameFocused}")
                    Text(
                        text = "알파벳 소문자와 숫자, 4~20자의 아이디를 입력해 주세요.",
//                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
//                    현재 아이디 정규표현식이 맞으면 checkid가 실행되어 버린다
//정규표현식에 맞으면 아이디 통신, 단 해당 textfield에포커스가 존재 할 동안에만
                }
                else if (usernameFocused == true){
                    checkId(username) { isSuccess ->
                        usernameResult = isSuccess
                        if (usernameResult == true) {
                            duplicatedUsername = true
                        } else {
                            duplicatedUsername = false
                        }
                    }
                }

                if (duplicatedUsername == true) {
                    Text(
                        text = "중복된 아이디입니다.",
//                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }

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
                    onValueChange = {
                        passwordConfirm = it },
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
                if (password != passwordConfirm) {
                    Text(
                        text = "동일한 비밀번호를 입력해주세요."
                    )
                }

                if (!passwordregex.matches(password)) {
                    Text(
                        text = "비밀번호는 8~16자의 영어와 숫자로만 입력해주세요.",
//                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )}


                    TextField(
                        value = email,
                        onValueChange = {
                            email = it
                            Log.d("response", "이메일 포커스 ${emailFocused}")

                        },
                        label = { Text(text = "이메일") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { emailFocused = if (it.isFocused) true else false },
                        singleLine = true,
                        maxLines = 1,

                        )
                    Text(
                        text = "계정 분실 시 본인인증 정보로 활용됩니다.",
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (!emailRegex.matches(email)) {
                        Text(
                            text = "알맞은 이메일 형식으로 입력해주세요.",
//                        color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .padding(start = 16.dp, top = 4.dp)

                        )

                    } else if(emailFocused == true) {
                        checkEmail(email) { isSuccess ->
                            emailResult = isSuccess
                            Log.d("response", "email중복 결과${emailResult}")
                            if (emailResult == true) {
                                duplicatedEmail = true
                            } else {
                                duplicatedEmail = false
                            }
                        }
                    }

                    if (duplicatedEmail == true) {
                        Text(
                            text = "중복된 이메일입니다.",
//                        color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }



                    TextField(
                        value = nickname,
                        onValueChange = {
                            nickname = it

                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),

                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged { nicknameFocused = if (it.isFocused) true else false
                                            Log.d("response", "닉네임 포커스${nicknameFocused}")
                                            }
                            .fillMaxWidth(),

                        label = { Text(text = "닉네임") },
                        singleLine = true,
                        maxLines = 1,
                    )

                    if (!nicknameregex.matches(nickname)) {
                        Log.d("response", "닉네임 포커스${nicknameFocused}")
                        Log.d("response", "${nickname}")
                        Log.d("response", "정규표현식 통과 못함")

                        Text(
                            text = "닉네임은 2~10자의 문자와 숫자로 만들어주세요!",
//                        color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )

                    } else if (nicknameFocused == true) {
                        checkNickname(nickname) { isSuccess ->
                            nicknameResult = isSuccess
                            Log.d("response", "${nicknameResult}")
                            if (nicknameResult == true) {
                                duplicatedNickname = true
                            } else {
                                duplicatedNickname = false
                            }
                        }
                    }

                    if (duplicatedNickname == true) {
                        Text(
                            text = "중복된 닉네임입니다.",
//                        color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }

                    Button(
                        onClick = {
                            // TODO: 회원가입 로직 구현
                            var result = false
                            // 회원가입이 완료되면 Snackbar 띄우기
                            Log.d("회원", "회원가입버튼누름")

                            singup(
                                SignupRequest(
                                    username = username,
                                    password = password,
                                    nickname = nickname,
                                    email = email
                                )
                            ) { isSuccess ->
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