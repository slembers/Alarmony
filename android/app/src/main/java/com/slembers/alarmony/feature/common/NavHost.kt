package com.slembers.alarmony.feature.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.group.GroupScreen
import com.slembers.alarmony.feature.group.InviteScreen
import com.slembers.alarmony.feature.group.SoundScreen


@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun NavController(
    navController : NavHostController = rememberNavController()
) {
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = NavItem.Group.route
    ) {
        composable( route = NavItem.Group.route ) { GroupScreen( navController = navController) }
        composable( route = NavItem.Sound.route ) { SoundScreen(navController) }
        composable( route = NavItem.GroupInvite.route ) { InviteScreen(navController) }
    }
}