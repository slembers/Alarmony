package com.slembers.alarmony.feature.user


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast


import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import com.slembers.alarmony.feature.user.Navigation

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

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.SnackbarHost
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.network.LoginData
import com.slembers.alarmony.feature.network.LoginResponse
import com.slembers.alarmony.feature.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


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
                Navigation()
                SignupScreen()
                mascott(drawing = R.drawable.mascot_foreground)
                logo(drawing = R.drawable.alarmony)
//                LoginScreen()

                Spacer(modifier = Modifier.height(8.dp))

                extra()


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
//
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




@Preview
@Composable
fun LoginScreen() {


    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    // 아이디와 비밀번호에 대한 상태를 저장할 mutableState 변수 선언
    val idState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = idState.value,
            onValueChange = { idState.value = it },
            label = { Text("ID") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        )

        TextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        )


//        아래는 로그인을 위한 통신로직을 RetrofitClient에서 가져와서 수행

        Button(
            onClick = {
                Log.d("TAG", "클릭버튼누르다!!")
//                getService(this) 오류가 나니 아래 코드와 같이 수정
//                Call, Callback이 아니라 retrofit2.Call, retrofit2.Callback
                val service = RetrofitClient.getService(this as Context)
                service.login(idState.value, passwordState.value).enqueue(object : retrofit2.Callback<LoginResponse?> {

                    override fun onResponse(call: retrofit2.Call<LoginResponse?>, response: retrofit2.Response<LoginResponse?>) {
                        Log.d("TAG", "리스폰스버튼누르다!!")
                        if (response.isSuccessful) {
                            Log.d("TAG", "성공")
                            val loginResponse = response.body()
//                            성공했을때
                            Toast.makeText(context, "성공 " + response, Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("TAG", "실패")
                            // 응답이 실패했을 때 처리할 작업
                            Toast.makeText(context, "실패 " + response.body(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<LoginResponse?>, t: Throwable) {
                        // 네트워크 오류 발생 시 처리할 코드 작성
                        Log.d("TAG", "뭐임?")
                        Toast.makeText(context, "실패 ", Toast.LENGTH_SHORT).show()
                    }
                })


            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Text("로그인")
        }
    }

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
