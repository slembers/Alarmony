package com.slembers.alarmony.feature.user
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {

        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)

        }
        composable(route = Screen.SettingScreen.route) {
            SettingScreen(navController = navController)

        }

    }
}

@Composable
fun MainScreen(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "하하 메인스크린")
    }

    Button(
        onClick = {
            navController.navigate(Screen.SettingScreen.route)
        },
        modifier = Modifier
            .width(100.dp)
            .padding(16.dp),

        ) {
        Text("메인스크린")

    }
}


@Composable
fun SettingScreen(navController: NavController) {

    Button(
        onClick = { navController.navigate(Screen.MainScreen.route) },
        modifier = Modifier
            .width(100.dp)
            .padding(16.dp),

        ) {
        Text("세팅스크린")
    }
}