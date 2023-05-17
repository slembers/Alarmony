package com.slembers.alarmony.feature.user


//import com.slembers.alarmony.feature.user.Navigation


//통신api
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.alarm.AlarmActivity
import com.slembers.alarmony.feature.alarm.AlarmApi.getAllAlarmsApi
import com.slembers.alarmony.feature.alarm.AlarmDto
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.notification.NotiApi
import com.slembers.alarmony.feature.notification.NotiApi.getAllNotisApi
import com.slembers.alarmony.feature.ui.common.AnimationRotation
import com.slembers.alarmony.network.repository.MemberService.getMyInfo
import com.slembers.alarmony.network.repository.MemberService.login
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalComposeUiApi::class)
//위 @OptIn(ExperimentalGlideComposeApi::class)이 회색으로 나오는 이유는
//사용되지 않아서가 아니라 실험적이고 불안정한 기능이기 때문이다.
@Composable
@ExperimentalMaterial3Api
fun LoginScreen(navController: NavController) {
    Log.d("email", "LoginScreen called")
//    val checkedState = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    // 아이디와 비밀번호에 대한 상태를 저장할 mutableState 변수 선언
    val idState = remember { mutableStateOf("") }
    //var idError = rememberSaveable  { mutableStateOf<Boolean>(false) }
    //var idError = false;
    var isIdError = remember { mutableStateOf(false) }
    var isPasswordError = remember { mutableStateOf(false) }

    //아이디랑 비밀번호가 정확하게 입력되었는지
    var isFilledId = remember { mutableStateOf(false) }
    var isFilledPassword = remember { mutableStateOf(false) }
    val passwordState = remember { mutableStateOf("") }
    var passwordVisibility = true
    var isSuccess = false
    var msg = ""

    var loading by remember { mutableStateOf(false) }

    val usernameRegex = "^[a-z0-9]{5,11}$".toRegex()
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z\\d]{8,16}\$".toRegex()
    val scrollState = rememberScrollState()



    BackOnPressed()
    Column(
        Modifier
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        mascott(drawing = R.drawable.mascot_foreground)
        logo(drawing = R.drawable.alarmony)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

        }

        Column {
            OutlinedTextField(
                value = idState.value,
                onValueChange = {
                    idState.value = it
                    //정규식이 불일치 할 경우
                    if(!usernameRegex.matches(it)){
                        isIdError.value = true;
                        isFilledId.value = false;
                    }else{
                        isIdError.value = false;
                        isFilledId.value = true;
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = Black,
                    errorBorderColor = "#EF2B2A".toColor()
                ),
                label = { Text("아이디") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),

                isError = isIdError.value,

                )
            if (isIdError.value) {
                Log.d("idError", isIdError.value.toString()+"아디디")
                Text(
                    text = "영문, 숫자를 조합해서 입력해주세요.(5~11자) ",
                    color = "#EF2B2A".toColor(),
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 18.dp)
                )
            }
        }

        Column {
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it

                    if(!passwordRegex.matches(it)){
                        isPasswordError.value = true;
                        isFilledPassword.value = false;
                    }else{
                        isPasswordError.value = false;
                        isFilledPassword.value = true;
                    }
                                },
                label = { Text("비밀번호") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Black,
                    unfocusedBorderColor = Black,
                    errorBorderColor = "#EF2B2A".toColor(),
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = isPasswordError.value,
                )
            if (isPasswordError.value) {
                Log.d("idError", isPasswordError.value.toString()+"비번")
                Text(
                    text = "영문, 숫자를 조합해서 입력해주세요.(8-16자) ",
                    color = "#EF2B2A".toColor(),
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 18.dp)
                )
            }


        }
        Button(
            onClick = {
                Log.d("확인", "${idState.value}, ${passwordState.value} +로그인")
                loading = true
                CoroutineScope(Dispatchers.Main).launch {
                    val result = login(
                        username = idState.value,
                        password = passwordState.value,
                        context
                    )
                    if (result) {
                        getMyInfo()
                        Log.d("StartPageActiviy","로그인 정보 저장 : ${MainActivity.prefs.getString("nickname","")}")
                    }
                    getAllAlarmsApi(context)
                    getAllNotisApi(context)
                    Log.d("INFO","result : $result")
                    if(result) {
                        NotiApi.sendAutoLogoutAndChangeToken()
                        val intent = Intent(context,MainActivity::class.java)
                        context.startActivity(intent)
                        (context as Activity).finish()
                        loading = false
                    }
                    loading = false
                }
            },
            enabled = isFilledId.value && isFilledPassword.value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 15.dp)
                .clip(RoundedCornerShape(20.dp)),

            colors = ButtonDefaults.buttonColors(
                backgroundColor = Black, // Set the background color of the button
                contentColor = Color.White // Set the text color of the buttonl
            )


        ) {
            Text("로그인")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(
                enabled = !loading,
                onClick = { navController.navigate(NavItem.Signup.route) },
                modifier = Modifier.size(width = 120.dp, height = 50.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Black // Set the font color of the button
                )) {
                Text(text = "회원가입")

            }
            TextButton(
                enabled = !loading,
                onClick = { navController.navigate(NavItem.FindIdActivity.route) },
                modifier = Modifier.size(width = 120.dp, height = 50.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Black // Set the font color of the button
                )

            ) {
                Text(text = "아이디 찾기")
            }

            TextButton(
                enabled = !loading,
                onClick = { navController.navigate(NavItem.FindPswdActivity.route) } ,
                modifier = Modifier.size(width = 120.dp, height = 50.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Black // Set the font color of the button
                )

            ) {
                Text(text = "비밀번호 찾기")
            }
        }
    }
    if(loading) {
        AnimationRotation()
    }

}

@Composable
fun mascott(drawing:Int) {
    val context = LocalContext.current
    Image(
        painter = painterResource(drawing),
        contentDescription = "mascott image",
        modifier = Modifier
            .padding(top = 100.dp, bottom = 20.dp)
            .clickable() {
                val alarmDto = AlarmDto(
                    1,
                    "테스트",
                    12,
                    20,
                    listOf(false, false, false, false, false, false, false),
                    "gmlgml",
                    4,
                    true,
                    true,
                    "하하"
                )
                val newIntent = Intent(context, AlarmActivity::class.java)
                newIntent.putExtra("alarmId", alarmDto.alarmId)
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(newIntent)
            }
    )

}

@Composable
fun logo(drawing:Int) {
    Image(painter = painterResource(id = R.drawable.alarmony),
        contentDescription = "mascott image",
                modifier = Modifier
                    .padding( bottom = 20.dp)
    )

}

@Composable
fun BackOnPressed() {
    val context = LocalContext.current
    var backPressedState by remember { mutableStateOf(true) }
    var backPressedTime = 0L

    BackHandler(enabled = backPressedState) {
        if(System.currentTimeMillis() - backPressedTime <= 2000L) {
            // 앱 종료
            (context as Activity).finish()
        } else {
            backPressedState = true
            Toast.makeText(context, "한 번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
