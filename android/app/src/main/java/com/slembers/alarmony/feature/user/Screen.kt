package com.slembers.alarmony.feature.user

sealed class Screen(val route: String) {

    object MainScreen : Screen("main_screen")
    object SettingScreen : Screen("setting_screen")
//    유저 설정과 기타 다른 설정들을 넣어서 경로를 설정할 수 있다.
}