package com.slembers.alarmony.feature.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.alarm.NotiListScreen
import com.slembers.alarmony.feature.screen.AlarmListScreen
import com.slembers.alarmony.feature.screen.GroupScreen
import com.slembers.alarmony.feature.screen.InviteScreen
import com.slembers.alarmony.feature.screen.SoundScreen
import com.slembers.alarmony.feature.user.AccountMtnc
import com.slembers.alarmony.feature.user.FindId
import com.slembers.alarmony.feature.user.Findpswd
import com.slembers.alarmony.feature.user.LoginScreen
import com.slembers.alarmony.feature.user.ProfileSetting
import com.slembers.alarmony.feature.user.SignupScreen
import com.slembers.alarmony.viewModel.GroupViewModel


@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun NavController(
    navController : NavHostController = rememberNavController()
) {

    val group : GroupViewModel = viewModel()

    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
//        startDestination = NavItem.Group.route
        startDestination = NavItem.AlarmListScreen.route
    ) {
        // 알람 목록 조회 페이지
        composable( route = NavItem.AlarmListScreen.route) {
            AlarmListScreen(navController)
        }
        // 알림 목록 조회 페이지
        composable( route = NavItem.NotiListScreen.route) {
            NotiListScreen(navController)
        }
        // 그룹생성 페이지
        composable( route = NavItem.Group.route ) { GroupScreen(navController, group) }
        composable( route = NavItem.Sound.route ) { SoundScreen( navController = navController ) }
        composable( route = NavItem.GroupInvite.route ) { InviteScreen(navController, group) }
        // 로그인 페이지
        composable( route = NavItem.LoginScreen.route) {LoginScreen(navController = navController)}
        composable( route = NavItem.FindIdActivity.route) {FindId(navController = navController) }
        // 회원가입 페이지
        composable( route = NavItem.Signup.route) { SignupScreen(navController = navController) }
        composable(NavItem.FindIdActivity.route) {
            FindId(navController = navController)

            }
            composable(NavItem.FindPswdActivity.route) {
                Findpswd(navController = navController)

            }
            composable(NavItem.ProfileActivity.route) {
                ProfileSetting(navController = navController)

            }

            composable(NavItem.AccountMtnc.route) {
                AccountMtnc(navController = navController)

            }


        }
}