package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.network.repository.MemberService
import com.slembers.alarmony.network.repository.MemberService.findPswd


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun Findpswd(navController: NavController) {

    var email by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    var isEmailError = remember { mutableStateOf(false) }
    var username by rememberSaveable { mutableStateOf("") }
    var isIdError = remember { mutableStateOf(false) }

    val (focusRequester) = FocusRequester.createRefs()
    val keyboardController = LocalSoftwareKeyboardController.current

    val localFocusManager = LocalFocusManager.current
    val keyboardActions = KeyboardActions(
        onNext = { localFocusManager.moveFocus(FocusDirection.Down) },
        onDone = {
            localFocusManager.clearFocus()
            keyboardController?.hide()
        }
    )
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
                modifier = Modifier.shadow(3.dp)
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
                    isIdError = isIdError,
                    keyboardActions = keyboardActions,
                    imeAction = ImeAction.Next
                )

                FindEmailTextField(
                    email = email,
                    onEmailChange = { email = it },
                    isEmailError = isEmailError,
                    keyboardActions = keyboardActions,
                    imeAction = ImeAction.Done
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
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    imeAction: ImeAction = ImeAction.Default,
) {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{2,})".toRegex()
    TitleText("이메일 *")
    OutlinedTextField(
        value = email,
        onValueChange = {
            onEmailChange(it)
            isEmailError.value = !emailRegex.matches(it)
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            errorBorderColor = "#EF2B2A".toColor()

        ),
        singleLine = true,
        maxLines = 1,
        isError = isEmailError.value,
        modifier = Modifier
            .fillMaxWidth(),
        keyboardActions = keyboardActions,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = imeAction
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
