package com.slembers.alarmony.feature.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.ui.group.GroupBottomButtom
import com.slembers.alarmony.feature.ui.group.GroupDialog
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.groupInvite.CurrentInvite
import com.slembers.alarmony.feature.ui.groupInvite.SearchInviteMember
import com.slembers.alarmony.viewModel.GroupViewModel

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun InviteScreen(
    navController : NavHostController = rememberNavController(),
    viewModel : GroupViewModel = viewModel()
) {

    val currentMembers = viewModel.members.observeAsState()
    val isClicked = remember { mutableStateOf(false)  }

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.GroupInvite.title,
                navClick = { navController.popBackStack() }
            )
        },
        containerColor = "#F9F9F9".toColor(),
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                content = {
                    CurrentInvite(currentMembers = currentMembers.value ?: mutableListOf())
                    SearchInviteMember(
                        viewModel = viewModel,
                        currentMembers = currentMembers.value ?: mutableListOf()
                    )
                }
            )
        }
    )
}