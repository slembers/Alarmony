package com.slembers.alarmony.feature.common

import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.alarm.NotiListScreen
import com.slembers.alarmony.feature.report.ReportDetailScreen
import com.slembers.alarmony.feature.report.ReportListScreen
import com.slembers.alarmony.feature.report.ReportScreen
import com.slembers.alarmony.feature.screen.AlarmListScreen
import com.slembers.alarmony.feature.screen.DetailsInviteScreen
import com.slembers.alarmony.feature.screen.GroupDetailsScreen
import com.slembers.alarmony.feature.user.AccountMtnc
import com.slembers.alarmony.viewModel.GroupDetailsViewModel


@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun NavController(

    intent: Intent, navController: NavHostController = rememberNavController()
) {

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
        startDestination = NavItem.AlarmListScreen.route
    ) {
        // 알람 목록 조회 페이지
        composable(route = NavItem.AlarmListScreen.route) {
            AlarmListScreen(navController)
        }
        // 알림 목록 조회 페이지
        composable(route = NavItem.NotiListScreen.route) {
            NotiListScreen(navController)
        }
        // 알람 프로필 페이지
        composable(NavItem.AccountMtnc.route) {
            AccountMtnc(navController = navController)
        }
        // 알람 상세 조회 페이지
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
        // 신고 페이지
        composable(NavItem.ReportPage.route) {
            ReportScreen(navController = navController)
        }
        // 신고 리스트
        composable(NavItem.ReportList.route) {
            ReportListScreen(navController = navController)
        }
        // 신고 상세보기
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
        // 그룹알람 상세 - 초대 페이지
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