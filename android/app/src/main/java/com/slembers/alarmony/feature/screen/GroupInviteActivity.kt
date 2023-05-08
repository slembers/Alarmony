package com.slembers.alarmony.feature.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.CurrentInvite
import com.slembers.alarmony.feature.ui.group.GroupBottomButtom
import com.slembers.alarmony.feature.ui.group.GroupDialog
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.group.SearchInviteMember
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.viewModel.GroupViewModel

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun InviteScreen(
    navController : NavHostController = rememberNavController(),
    group : GroupViewModel = viewModel()
) {

    val currentMembers = remember { navController.previousBackStackEntry?.savedStateHandle?.get<Set<MemberDto>>("members") }
    val isClicked = remember { mutableStateOf(false)  }

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.GroupInvite.title,
                navcontroller = navController
            )
        },
        bottomBar = {
            GroupBottomButtom(
                text = "저장",
                onClick = { isClicked.value = true }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                content = {
                    CurrentInvite(currentMembers ?: setOf())
                    SearchInviteMember(currentMembers ?: setOf())
                }
            )
            if(isClicked.value) {
                GroupDialog(
                    isClicked = isClicked,
                    navController = navController,
                    group = group
                )
            }
        }
    )
}