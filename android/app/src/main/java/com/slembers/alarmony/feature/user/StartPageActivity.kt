package com.slembers.alarmony.feature.user


import android.os.Bundle
import android.util.Log


import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode.Companion.Screen


import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.NavItem

//import com.slembers.alarmony.feature.user.Navigation


//통신api
import com.slembers.alarmony.network.repository.MemberService.login



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

//                SignupScreen()
//                Findpswd()
//                FindId()()
//                ProfileSetting()
//                AccountMtnc()

//                LoginScreen()
//                Navigation()






            }
        }
    }
}



@Preview
@Composable

//fun extra(navController: NavController) {
fun extra() {




}





@Composable
fun LoginScreen(navController: NavController) {


    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    // 아이디와 비밀번호에 대한 상태를 저장할 mutableState 변수 선언
    val idState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        mascott(drawing = R.drawable.mascot_foreground)
        logo(drawing = R.drawable.alarmony)
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
//MutableState<String>와 String은 형식이 다르기에 String 값을 보내기 위해 .value를 붙여준다.
            onClick = {
                Log.d("확인", "${idState.value}, ${passwordState.value} +로그인")
                      login(idState.value, passwordState.value)

            },
            modifier = Modifier
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Text("로그인")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
//            .fillMaxWidth(),
            ,

            horizontalArrangement = Arrangement.SpaceAround
        ) {
            TextButton(onClick = {
                navController.navigate(NavItem.Signup.route)

            }) {
                Text(text = "회원가입 |")

            }

            TextButton(onClick = {
                navController.navigate(NavItem.FindIdActivity.route)
            }) {
                Text(text = "아이디 찾기 |")
            }

            TextButton(onClick = {
                navController.navigate(NavItem.FindPswdActivity.route)
            }) {
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

            .padding(top = 100.dp)
    )

}

@Composable
fun logo(drawing:Int) {
    Image(painter = painterResource(id = R.drawable.alarmony), contentDescription = "mascott image")
}
