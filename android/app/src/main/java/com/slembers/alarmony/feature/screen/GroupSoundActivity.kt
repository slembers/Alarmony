package com.slembers.alarmony.feature.screen


import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.view.SoundChooseGridView
import com.slembers.alarmony.feature.ui.group.GroupBottomButtom
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.model.db.SoundItem
import com.slembers.alarmony.util.groupSoundInfos
import com.slembers.alarmony.viewModel.GroupViewModel

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun SoundScreen(
    navController : NavHostController = rememberNavController(),
    viewModel : GroupViewModel,
) {
    val soundItems : List<SoundItem> = groupSoundInfos()

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.Sound.title,
                navClick = {
                    navController.popBackStack()
                    soundAllStop(soundItems)
                }
            )
        },
        bottomBar = {
            GroupBottomButtom(
                text = "저장",
                onClick = {
                    Log.d("INFO","선택한 음악 : ${viewModel.sound}")
                    navController.popBackStack()
                    soundAllStop(soundItems)
                }
            )
        },
        content = {
                innerPadding ->
            Column( modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {
                SoundChooseGridView(
                    soundItems = soundItems,
                    viewModel = viewModel
                )
            }
        }
    )
}

fun soundAllStop( soundItems : List<SoundItem> ) {
    for ( item in soundItems) {
        item.isPlaying = false
        item.soundMp3Content?.release()
    }
}