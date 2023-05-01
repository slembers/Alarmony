package com.slembers.alarmony.feature.group

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.SoundChooseGrid
import com.slembers.alarmony.model.db.SoundItem

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun SoundScreen(navController : NavHostController = rememberNavController()) {

    val soundList : List<SoundItem> = (1..20).map {
        SoundItem(
            painterResource(id = R.drawable.main_app_image_foreground),
            "sound$it") }.toList()

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.Sound.title,
                navEvent = navController
            )
        },
        bottomBar = {
            GroupBottomButtom(text = "저장" )
        },
        content = { innerPadding ->
            Column( modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize() )
            {
                CardBox(
                    title = { CardTitle(title = "사용가능한 알람") },
                    content = { SoundChooseGrid(
                        modifier = Modifier.fillMaxWidth(),
                        itemList = soundList
                    ) }
                )
            }
        }
    )
}