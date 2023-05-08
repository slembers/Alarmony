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
        title = "알람소리",
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

    object MainScreen : NavItem("main_screen", "main_screen", Icons.Default.Home)
    object SettingScreen : NavItem("setting_screen", "setting_screen",Icons.Default.Home)
    object LoginScreen : NavItem("login_screen","login_screen" ,Icons.Default.Home)

    object FindIdActivity : NavItem("findid_screen", "findid_screen",Icons.Default.Home)
    object FindPswdActivity : NavItem("findpswd_screen", "findpswd_screen",Icons.Default.Home)
    object Signup : NavItem("signup_screen", "signup_screen",Icons.Default.Home)
    object ProfileActivity : NavItem("profile_screen", "profile_screen",Icons.Default.Home)
    object AccountMtnc : NavItem("accountmtnc_screen", "accountmtnc_screen",Icons.Default.Home)

}