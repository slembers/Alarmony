package com.slembers.alarmony.feature.user


//import com.slembers.alarmony.feature.user.Navigation


//통신api
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalComposeUiApi::class)
//위 @OptIn(ExperimentalGlideComposeApi::class)이 회색으로 나오는 이유는
//사용되지 않아서가 아니라 실험적이고 불안정한 기능이기 때문이다.
@Composable
@ExperimentalMaterial3Api
fun LoginScreen(navController: NavController) {
//    val checkedState = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()
    // 아이디와 비밀번호에 대한 상태를 저장할 mutableState 변수 선언
    val idState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    var isSuccess = false
    var msg = ""
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        mascott(drawing = R.drawable.mascot_foreground)
        logo(drawing = R.drawable.alarmony)
        TextField(
            value = idState.value,
            onValueChange = { idState.value = it
                            },
            label = { Text("ID") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,

                ),
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
//                .onFocusChanged{ keyboardController?.hide()}
                .onFocusChanged{ /* 아이디의 중복api넣기 */}


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
//                .onFocusChanged{ keyboardController?.hide()}
        )
//아래는 자동로그인 체크박스

//        Row(modifier = Modifier.padding(0.dp)) {
//            // Checkbox Composable을 사용하여 체크박스 UI를 생성
//            Checkbox(
//                checked = checkedState.value,
//                onCheckedChange = {
//                    checkedState.value = it
//                    if (it) {
////                        prefs.setString("autoLogin", "true")
////                        prefs.setBoolean("auto_login", true)
//                      Log.d("체크박스", "자동 로그인 온")
//                      Log.d("체크박스", "${prefs.getBoolean("auto_login", false)}")
//                    } else {
////                        prefs.setBoolean("auto_login", false)
//                        Log.d("체크박스", "자동 로그인 오프")
//                        Log.d("체크박스", "${prefs.getBoolean("auto_login", false)}")
//
//                    }
//                }
//            )
//            Text(text = "자동 로그인 ")
//        }



//        아래는 로그인을 위한 통신로직을 RetrofitClient에서 가져와서 수행

        Button(
//MutableState<String>와 String은 형식이 다르기에 String 값을 보내기 위해 .value를 붙여준다.
            onClick = {
                Log.d("확인", "${idState.value}, ${passwordState.value} +로그인")
                CoroutineScope(Dispatchers.Main).launch {
                    val result = login(
                        username = idState.value,
                        password = passwordState.value
                    )
                    putRegistTokenAfterSignIn()
                    Log.d("INFO","result : $result")
                    if(result) navController.navigate(NavItem.AlarmListScreen.route)
                }
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
