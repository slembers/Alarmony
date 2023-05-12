package com.slembers.alarmony.feature.user


//import com.slembers.alarmony.feature.user.Navigation


//통신api
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.network.repository.MemberService.login
import com.slembers.alarmony.network.repository.MemberService.putRegistTokenAfterSignIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.slembers.alarmony.MainActivity
import kotlin.math.log


enum class Routes() {
    Signup,
    Setting
}


class StartPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )  {


            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalComposeUiApi::class)
//위 @OptIn(ExperimentalGlideComposeApi::class)이 회색으로 나오는 이유는
//사용되지 않아서가 아니라 실험적이고 불안정한 기능이기 때문이다.
@Composable
@ExperimentalMaterial3Api
@Preview(showBackground = true)
fun LoginScreen(navController: NavController = rememberNavController()) {
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

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("AlarmListScreen","[알람목록] Activity 이동성공")
        }
    }

    val usernameRegex = "^[a-z0-9]{4,20}$".toRegex()
    val passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])[a-zA-Z\\d]{8,16}\$".toRegex()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        mascott(drawing = R.drawable.mascot_foreground)
        logo(drawing = R.drawable.alarmony)

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
                    errorBorderColor = Red
                ),
                label = { Text("아이디") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),

                isError = isIdError.value,

                )
            if (isIdError.value) {
                Log.d("idError", isIdError.value.toString())
                Text(
                    text = "영소문, 숫자를 조합해서 입력해주세요.(4~20자) ",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        Column {
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it
                  //  isPasswordError.value = !passwordRegex.matches(it)

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
                    errorBorderColor = Red,
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                isError = isPasswordError.value,
                )
            if (isPasswordError.value) {
                Text(
                    text = "영문, 숫자를 조합해서 입력해주세요. (8-16자) ",
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }


        }
        Button(
            onClick = {
                Log.d("확인", "${idState.value}, ${passwordState.value} +로그인")
                CoroutineScope(Dispatchers.Main).launch {
                    val result = login(
                        username = idState.value,
                        password = passwordState.value,
                        context
                    )

                    Log.d("INFO","result : $result")
                    if(result) { (context as Activity).finish() }
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
            TextButton(onClick = {
                navController.navigate(NavItem.Signup.route)

            },
                modifier = Modifier.size(width = 120.dp, height = 50.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Black // Set the font color of the button
                )) {
                Text(text = "회원가입")

            }
            TextButton(onClick = {
                navController.navigate(NavItem.FindIdActivity.route)
            },

                modifier = Modifier.size(width = 120.dp, height = 50.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Black // Set the font color of the button
                )

            ) {
                Text(text = "아이디 찾기")
            }

            TextButton(onClick = {
                navController.navigate(NavItem.FindPswdActivity.route)
            } ,
                modifier = Modifier.size(width = 120.dp, height = 50.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Black // Set the font color of the button
                )

            ) {
                Text(text = "비밀번호 찾기")
            }
        }
    }


}

@Composable
fun mascott(drawing:Int) {
    Image(
        painter = painterResource(drawing),
        contentDescription = "mascott image",
        modifier = Modifier
            .padding(top = 130.dp , bottom = 20.dp)
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
