package com.slembers.alarmony.feature.group

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.ui.AlamonyBottomButton
import com.slembers.alarmony.feature.common.ui.AlamonyTopBar
import com.slembers.alarmony.feature.common.ui.compose.CurrentInvite
import com.slembers.alarmony.feature.common.ui.compose.SearchInviteMember

class GroupInviteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun InviteScreen() {
    Scaffold (
        topBar = { AlamonyTopBar( title = "그룹원 추가") },
        bottomBar = { AlamonyBottomButton(text = "저장") },
        content = {innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                content = {
                    CurrentInvite()
                    SearchInviteMember()
                }
            )
        }
    )
}