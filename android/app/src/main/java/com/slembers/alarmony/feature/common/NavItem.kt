package com.slembers.alarmony.feature.common

import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val title : String,
    val route : String,
    val routeWithPostFix : String,
    val image : ImageVector,
    val badgeCount : Int = 0,
)

val items = listOf(
    NavItem(
        NavItems.Group.title,
        NavItems.Group.route,
        NavItems.Group.routeWithPostFix,
        NavItems.Group.image,
    ),
    NavItem(
      NavItems.Sound.title,
      NavItems.Sound.route,
      NavItems.Sound.routeWithPostFix,
      NavItems.Sound.image,
    ),
    NavItem(
        NavItems.Member.title,
        NavItems.Member.route,
        NavItems.Member.routeWithPostFix,
        NavItems.Member.image,
    ),
)