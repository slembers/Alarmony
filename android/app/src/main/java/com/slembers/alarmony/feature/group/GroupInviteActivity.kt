package com.slembers.alarmony.feature.group

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.ui.compose.CurrentInvite
import com.slembers.alarmony.feature.common.ui.compose.SearchInviteMember

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun InviteScreen(navController : NavHostController = rememberNavController()) {
    Column(
        content = {
            CurrentInvite()
            SearchInviteMember()
        }
    )
}