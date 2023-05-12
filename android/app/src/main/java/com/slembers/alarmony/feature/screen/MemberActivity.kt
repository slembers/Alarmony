package com.slembers.alarmony.feature.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.user.FindId
import com.slembers.alarmony.feature.user.Findpswd
import com.slembers.alarmony.feature.user.LoginScreen
import com.slembers.alarmony.feature.user.SignupScreen

@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class MemberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            val navController : NavHostController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = NavItem.LoginScreen.route
            ) {
                // 로그인 페이지
                composable(route = NavItem.LoginScreen.route ) {
                    LoginScreen(navController = navController)
                }
                // 회원가입 페이지
                composable( route = NavItem.Signup.route) {
                    SignupScreen(navController = navController) }
                // 아이디 찾기 페이지
                composable(NavItem.FindIdActivity.route) {
                    FindId(navController = navController)
                }
                // 비밀번호 찾기
                composable(NavItem.FindPswdActivity.route) {
                    Findpswd(navController = navController)
                }
            }
        }
    }
}