package com.slembers.alarmony.feature.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key.Companion.Home

enum class NavItems(
    val title : String,
    val route : String,
    val routeWithPostFix : String,
    val image : ImageVector,
    val badgeCount : Int = 0,
) {

    Group(
        route = "group",
        routeWithPostFix = Group.route,
        title = "그룹생성",
        image = Icons.Default.Home
    ),

    Sound(
        route = "sound",
        routeWithPostFix = Sound.route,
        title = "알람소리",
        image = Icons.Default.ArrowForwardIos
    ),

    Member(
        route = "member",
        routeWithPostFix = Member.route,
        title = "그룹원 초대",
        image = Icons.Default.ArrowForwardIos
    ),

}