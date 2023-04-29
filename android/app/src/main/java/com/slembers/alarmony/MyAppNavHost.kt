package com.slembers.alarmony

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.group.GroupToolBar
import com.slembers.alarmony.feature.user.MainScreen
import com.slembers.alarmony.feature.user.SettingScreen

@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class MyAppNavHost : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun MyApp(navController : NavHostController = rememberNavController()) {
    Scaffold(
        topBar = { GroupToolBar() }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavItem.Group.route,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(route = NavItem.Group.route) {
                MainScreen(navController = navController)

            }
            composable(route = NavItem.Sound.route) {
                SettingScreen(navController = navController)

            }

        }

    }

}