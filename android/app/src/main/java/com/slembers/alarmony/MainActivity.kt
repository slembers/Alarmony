package com.slembers.alarmony

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavController
import com.slembers.alarmony.feature.common.NavController2
import com.slembers.alarmony.network.repository.MemberService.autoLogin
import com.slembers.alarmony.util.PresharedUtil



@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class MainActivity : AppCompatActivity() {


    companion object {
        lateinit var prefs : PresharedUtil
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      SharedPreferences 클래스는 앱에 있는 다른 Class보다 먼저 생성되어야함
        prefs = PresharedUtil(application)
        val auto_login = prefs.getBoolean("auto_login", false)
        if (auto_login == true) {
            /**/
            autoLogin(prefs.getString("id", ""),
                prefs.getString("password", ""),
            ) { resultText, accessToken, refreshToken ->
                Log.d("자동 로그인", "자동로그인 체크중")
                prefs.setString("accessToken", accessToken)
                prefs.setString("refreshToken", refreshToken)
                val token = prefs.getString("accessToken", "기본값")
                Log.d("getstring확인", "${token}")
//이제 여기서 navigation이든 intent등 활용해서 화면을 전환해야한다.
//                하지만 Mainactivity라서 navigaiton은 사용할 수 없다????
//                setcontent부분에 로그인 화면이 아니라 메인 화면으로 가게끔 만들어야 한다.
//                로그인 화면이 아닌 바로 메인 화면 activity가 실행되게끔 만들어야 한다.
//                Navcontroller2를 만들어서 해버리자
                setContent {
                    NavController2()
                }

            }
//            자동로그인이 되지 않으면 정상적으로 Composable LoginScreen()이 라우팅 되는 Navcontroller를 set해준다.
        } else {
            setContent {
                NavController()
            }
        }


    }
}