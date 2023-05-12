package com.slembers.alarmony.feature.common

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.feature.alarm.NotiListScreen
import com.slembers.alarmony.feature.report.ReportDetailScreen
import com.slembers.alarmony.feature.report.ReportListScreen
import com.slembers.alarmony.feature.report.ReportScreen
import com.slembers.alarmony.feature.screen.AlarmListScreen
import com.slembers.alarmony.feature.screen.DetailsInviteScreen
import com.slembers.alarmony.feature.screen.GroupDetailsScreen
import com.slembers.alarmony.feature.user.AccountMtnc
import com.slembers.alarmony.feature.user.FindId
import com.slembers.alarmony.feature.user.Findpswd
import com.slembers.alarmony.feature.user.LoginScreen
import com.slembers.alarmony.feature.user.ProfileSetting
import com.slembers.alarmony.feature.user.SignupScreen
import com.slembers.alarmony.feature.user.LoginScreen
import com.slembers.alarmony.feature.user.StartPageActivity
import com.slembers.alarmony.network.api.AlarmonyServer
import com.slembers.alarmony.network.repository.MemberService.reissueToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import com.slembers.alarmony.viewModel.GroupDetailsViewModel


@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun NavController(
    intent: Intent, navController: NavHostController = rememberNavController()
) {


    //TODO Refresh Token을 전송한다.
    val accessToken = MainActivity.prefs.getString("accessToken", "")
    val refreshToken = MainActivity.prefs.getString("refreshToken", "")
    var username = MainActivity.prefs.getString("username", "")
    username =""
    Log.d("refresh","[알람목록] ${username} ${refreshToken}")

    var dasdsa = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("AlarmListScreen","[알람목록] Activity 이동성공")
        }
    }

    // 유저아이디나 Refresh토큰이 없는경우 -> 로그인 창으로 이동
    if (refreshToken.isBlank() || username.isBlank()) {
        Log.d("refresh","로그인 다시해야됨")
        //로그아웃 -> 로그인 페이지로 이동시킨다.
     //   Intent(dasdsa,StartPageActivity::class.java)
        //launcher.launch( Intent(dasdsa,StartPageActivity::class.java))
    } else {

        LaunchedEffect(Unit) {
            if (!reissueToken(username, refreshToken)) {
                Log.d("refresh","ㅇㅁㄴㅇㄴㅁㅇㄴㅁㅇㄴㅁ")
                //로그인 페이지로 이동
             //  Intent(dasdsa,StartPageActivity::class.java)
             //   launcher.launch( Intent(dasdsa,StartPageActivity::class.java))
            }

        }
    }

    Log.d("token","[로그인] $accessToken !!")
    val startDestinate = if(accessToken.isNotBlank()) NavItem.AlarmListScreen.route else NavItem.LoginScreen.route

    val context = LocalContext.current
    var details : GroupDetailsViewModel = viewModel(
        factory = GroupDetailsViewModel.GroupViewModelFactory(
            application = context.applicationContext as Application
        )
    )
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
//        startDestination = NavItem.Group.route
        startDestination = startDestinate
    ) {
        // 알람 목록 조회 페이지
        composable(route = NavItem.AlarmListScreen.route) {
            AlarmListScreen(navController)
        }
        // 알림 목록 조회 페이지
        composable(route = NavItem.NotiListScreen.route) {
            NotiListScreen(navController)
        }
        // 로그인 페이지
        composable(route = NavItem.LoginScreen.route) { LoginScreen(navController = navController) }
        composable(route = NavItem.FindIdActivity.route) { FindId(navController = navController) }
        // 회원가입 페이지
        composable(route = NavItem.Signup.route) { SignupScreen(navController = navController) }
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


        composable(
            route = "${NavItem.GroupDetails.route}/{alarmId}",
            arguments = listOf(
                navArgument("alarmId") {
                    type = NavType.LongType
                }
            )
        ) { entry ->
            val alarmId = entry.arguments?.getLong("alarmId")
            GroupDetailsScreen(
                navController,
                details,
                alarmId
            )
        }
        composable(NavItem.ReportPage.route) {
            ReportScreen(navController = navController)
        }

        composable(NavItem.ReportList.route) {
            ReportListScreen(navController = navController)
        }

        composable(
            route = "${NavItem.ReportDetail.route}/{reportId}",
            arguments = listOf(
                navArgument("reportId"){
                    type = NavType.LongType
                }
            )
        ) {entry ->
            val reportId = entry.arguments?.getLong("reportId")
            ReportDetailScreen(navController, reportId)
        }

        composable(
            route = NavItem.GroupDetailsInvite.route + "/{alarmId}",
            arguments = listOf(
                navArgument("alarmId") {
                    type = NavType.LongType
                }
            )
        ) { entry ->
            val alarmId = entry.arguments?.getLong("alarmId")
            DetailsInviteScreen(
                navController = navController,
                viewModel = details,
                alarmId = alarmId
            )
        }
    }

    val screen: String? = intent.getStringExtra("GO")
    if (screen != null && screen == "Noti") {
        navController.navigate(NavItem.NotiListScreen.route)
    }
}


//
//@Composable
//@ExperimentalMaterial3Api
//@ExperimentalGlideComposeApi
//fun NavController2(
//    navController : NavHostController = rememberNavController()
//) {
//
//    val groupModel : GroupViewModel = viewModel()
//
//    NavHost(
//        modifier = Modifier.fillMaxSize(),
//        navController = navController,
////        startDestination = NavItem.Group.route
//        startDestination = NavItem.AccountMtnc.route
////        startDestination = NavItem.Group.route
//    ) {
//
//        // 그룹생성 페이지
//        composable( route = NavItem.Group.route ) {
//            GroupScreen( navController = navController, groupModel )
//        }
//        composable( route = NavItem.Sound.route ) { SoundScreen(navController) }
//        composable( route = NavItem.GroupInvite.route ) { navBackStackEntry ->
//            InviteScreen(navController) }
//        // 로그인 페이지
//        composable( route = NavItem.LoginScreen.route) {LoginScreen(navController = navController)}
//        composable( route = NavItem.FindIdActivity.route) {FindId(navController = navController) }
//        // 회원가입 페이지
//        composable( route = NavItem.Signup.route) { SignupScreen(navController = navController) }
//        composable(NavItem.FindIdActivity.route) {
//            FindId(navController = navController)
//
//        }
//        composable(NavItem.FindPswdActivity.route) {
//            Findpswd(navController = navController)
//
//        }
//        composable(NavItem.ProfileActivity.route) {
//            ProfileSetting(navController = navController)
//
//        }
//
//        composable(NavItem.AccountMtnc.route) {
//            AccountMtnc(navController = navController)
//
//        }
//
//
//
//    }
//
//
//
//}