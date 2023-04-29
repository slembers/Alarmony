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

    object GroupMember : NavItem(
        route = "groupMember",
        title = "그룹초대",
        image = Icons.Default.ArrowForwardIos
    )
}