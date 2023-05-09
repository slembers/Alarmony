package com.slembers.alarmony.feature.screen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.view.SoundChooseGridView
import com.slembers.alarmony.feature.ui.group.GroupBottomButtom
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.model.db.SoundItem
import com.slembers.alarmony.util.groupSoundInfos

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun SoundScreen(navController : NavHostController = rememberNavController()) {
    val soundItems : List<SoundItem> = groupSoundInfos()

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.Sound.title,
                navClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            GroupBottomButtom(text = "저장" )
        },
        content = {
                innerPadding ->
            Column( modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                SoundChooseGridView(soundItems = soundItems)
            }
        }
    )
}