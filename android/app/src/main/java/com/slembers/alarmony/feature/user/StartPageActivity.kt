package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.os.Bundle

import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
//import androidx.compose.foundation.gestures.ModifierLocalScrollableContainerProvider.value
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
//import androidx.constraintlayout.compose.ConstraintLyaout
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextField
import androidx.compose.ui.draw.clip


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.slembers.alarmony.R

//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

//d아래는 통신을 위한 레트로핏, 오류때문에 주석처리

//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory

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
//                    Image(painter = painterResource(id = R.drawable.alarmony),
//                        contentDescription = "sd",
//                            modifier = Modifier
//                            .height(200.dp)
//                        .fillMaxWidth()
//                        .padding(top = 16.dp, bottom = 16.dp))
                Navigation()
                mascott(drawing = R.drawable.mascot_foreground)
                logo(drawing = R.drawable.alarmony)
                IdForm()
                Spacer(modifier = Modifier.height(8.dp))
                PasswordForm()
                extra()
                SetBtn()
                btn()
            }
        }
    }
}

@Preview
@Composable
fun extra() {



    Row(
        modifier = Modifier
//            .fillMaxWidth(),
        ,

        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TextButton(onClick = { /* 회원가입 버튼 클릭 시 처리할 동작 */ }) {
            Text(text = "회원가입 |")
        }

        TextButton(onClick = { /* 아이디 찾기 버튼 클릭 시 처리할 동작 */ }) {
            Text(text = "아이디 찾기 |")
        }

        TextButton(onClick = { /* 비밀번호 찾기 버튼 클릭 시 처리할 동작 */ }) {
            Text(text = "비밀번호 찾기")
        }
    }
}








@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun Loginform() {
    Text(text = "Loginform")
//아이디와 비밀번호를 저장하는 state객체


    //아이디 ui필드 구성

}
//아이디 입력창
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun IdForm() {
    val ID = remember { mutableStateOf("") }
    val shape = RoundedCornerShape(20.dp)
    TextField(
        value = ID.value,
        onValueChange = { ID.value = it },
        label = { Text("ID") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),

        modifier = Modifier
            .padding(top = 100.dp)
            .fillMaxWidth()
            .padding(16.dp)
            .clip(shape)
//            .graphicsLayer(
//                shadowElevation = 4.dp.value,
//                shape = shape,
//                clip = false
//            )

    )
}

//비밀번호 입력창
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PasswordForm(){
    val Password = remember { mutableStateOf("") }
    val shape = RoundedCornerShape(20.dp)
    TextField(
        value = Password.value,
        onValueChange = { Password.value = it },
        label = { Text("Password") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(shape)
//            .graphicsLayer(
//                shadowElevation = 4.dp.value,
//                shape = shape,
//                clip = false
//            )

    )
}

// 로그인 버튼 UI 구성




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen() {
    Scaffold(
        topBar = { /* 상단 바 UI 구성 */ },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // 로그인 폼 UI 구성
            }
        }
    )
}

@Composable
fun mascott(drawing:Int) {
    Image(
        painter = painterResource(drawing),
        contentDescription = "mascott image",
        modifier = Modifier

            .padding(top = 100.dp)
    )

}

@Composable
fun logo(drawing:Int) {
    Image(painter = painterResource(id = R.drawable.alarmony), contentDescription = "mascott image")
}



@Preview
@Composable
fun BtnForm() {
    Button(
        onClick = { /* 로그인 버튼 클릭 시 처리 */ },
        modifier = Modifier
            .width(100.dp)
            .padding(16.dp),

        ) {
        Text("로그인")
    }
}
@Preview
@Composable
fun SetBtn() {


    Button(
        onClick = {

        },
        modifier = Modifier
            .width(100.dp)
            .padding(16.dp),

        ) {
        Text("설정하기")
    }
}


@Preview
@Composable
fun btn() {
    val shape = RoundedCornerShape(20.dp)
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .padding(top = 40.dp)
            .clip(shape)
            .height(40.dp)
            .width(200.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF66BBFF), // 하늘색
            contentColor = Color.White // 흰색
        )
    ) {
        Text(text = "로그인")
    }
}

@Preview
@Composable
fun btn2() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .height(40.dp)
            .width(200.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFF66BBFF), // 하늘색
            contentColor = Color.White // 흰색
        )
    ) {
        Text(text = "회원가입")
    }
}
//interface ApiService {
//    @POST("login")
//    suspend fun login(@Body credentials: LoginCredentials): Response<LoginResponse>
//}
//
//data class LoginCredentials(
//    val username: String,
//    val password: String
//)
//
//data class LoginResponse(
//    val token: String
//)
//
//val retrofit = Retrofit.Builder()
//    .baseUrl("http://example.com/")
//    .addConverterFactory(GsonConverterFactory.create())
//    .build()
//
//val apiService = retrofit.create(ApiService::class.java)
//
//fun login(username: String, password: String) {
//    GlobalScope.launch(Dispatchers.IO) {
//        try {
//            val response = apiService.login(LoginCredentials(username, password))
//            if (response.isSuccessful) {
//                val token = response.body()?.token
//                // 서버에서 반환한 토큰을 사용하여 추가적인 작업 수행
//            } else {
//                // 서버에서 오류 응답 반환 시 처리할 작업 수행
//            }
//        } catch (e: Exception) {
//            // 네트워크 오류 등 예외 발생 시 처리할 작업 수행
//        }
//    }
//}
