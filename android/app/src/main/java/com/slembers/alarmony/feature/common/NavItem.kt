package com.slembers.alarmony.feature.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem (
    val title : String,
    val route : String,
    val image : ImageVector,
) {
    object Group : NavItem(
        route = "group",
        title = "그룹생성",
        image = Icons.Default.Home
    )

    object Sound : NavItem(
        route = "sound",
        title = "알람음 선택",
        image = Icons.Default.ArrowForwardIos
    )

    object GroupInvite : NavItem(
        route = "groupMember",
        title = "그룹초대",
        image = Icons.Default.ArrowForwardIos
    )

    object AlarmListScreen : NavItem(
        route = "alarmListScreen",
        title = "alarmony",
        image = Icons.Default.Home
    )

    object NotiListScreen : NavItem(
        route = "NotiListScreen",
        title = "알림",
        image = Icons.Default.ArrowForwardIos
    )

    object MainScreen : NavItem(
        route = "main_screen",
        title = "main_screen",
        image = Icons.Default.Home)
    object SettingScreen : NavItem(
        route = "setting_screen",
        title = "setting_screen",
        image = Icons.Default.Home)
    object LoginScreen : NavItem(
        route = "login_screen",
        title = "login_screen" ,
        image = Icons.Default.Home)

    object FindIdActivity : NavItem(
        route = "findid_screen",
        title = "findid_screen",
        image = Icons.Default.Home)
    object FindPswdActivity : NavItem(
        route = "findpswd_screen",
        title = "findpswd_screen",
        image = Icons.Default.Home)
    object Signup : NavItem(
        route = "signup_screen",
        title = "signup_screen",
        image = Icons.Default.Home)
    object ProfileActivity : NavItem(
        route = "profile_screen",
        title = "profile_screen",
        image = Icons.Default.Home)
    object AccountMtnc : NavItem(
        route = "accountmtnc_screen",
        title = "accountmtnc_screen",
        image = Icons.Default.Home)

    object GroupDetails : NavItem(
        route = "GroupDetails",
        title = "알람 상세",
        image = Icons.Default.ArrowForwardIos
    )

    object ReportPage : NavItem(
        route = "ReportDetails",
        title = "신고하기",
        image = Icons.Default.Home
    )

    object ReportList : NavItem(
        route = "ReportList",
        title = "신고목록",
        image = Icons.Default.Home
    )

    object ReportDetail : NavItem(
        route = "ReportDetail",
        title = "신고상세",
        image = Icons.Default.Home
    )

    object GroupDetailsInvite : NavItem(
        route = "GroupInvteDetails",
        title = "알람초대",
        image = Icons.Default.ArrowForwardIos
    )
}