package com.slembers.alarmony.feature.common

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.group.GroupScreen

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun Navigation(navController : NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavItems.Group.routeWithPostFix
    ) {
        composable( route = NavItems.Group.routeWithPostFix ) { GroupScreen(navController) }
    }
}