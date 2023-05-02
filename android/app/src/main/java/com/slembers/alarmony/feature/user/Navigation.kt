package com.slembers.alarmony.feature.user
import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

//네비게이션 사용법
@Composable
fun Navigation() {
//    네비게이션 객체 생성
    val navController = rememberNavController()
//    초기 네비게이션 상태 설정, 초기값은 Screen의 MainScreen으로
//    그 다음 각각의 네비게이션 들을 아래와 같이 정의한다.
//    각각의 네비게이션 콤포저블들의 상세정의는 다음 콤포저블에서 행한다.
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {

        composable(Screen.MainScreen.route) {
            toMainScreen(navController = navController)

        }
        composable(Screen.SettingScreen.route) {
            toSettingScreen(navController = navController)

        }

    }
}

@Composable
fun toMainScreen(navController: NavController) {

    Text(text = "하하 메인스크린")

    Button(
        onClick = {
            navController.navigate(Screen.SettingScreen.route){

            }
        },
        modifier = Modifier
            .width(100.dp)
            .padding(16.dp)
    ) {
        Text("메인스크린")
    }

}

@Composable
fun toSettingScreen(navController: NavController) {

    Text(text = "세팅스크린")

    Button(
        onClick = { navController.navigate(Screen.MainScreen.route) },
        modifier = Modifier
            .width(100.dp)
            .padding(16.dp)
    ) {
        Text("세팅스크린")
    }

}