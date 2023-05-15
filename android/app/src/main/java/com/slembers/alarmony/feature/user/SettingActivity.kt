package com.slembers.alarmony.feature.user


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.ui.profilesetting.SettingView

@Preview
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AccountMtnc(navController: NavController = rememberNavController()) {

    val Notichecked = remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.background
            )
        },
        containerColor = "#F9F9F9".toColor(),
        content = { innerPadding ->
            SettingView(
                navController,
                Modifier
                    .fillMaxSize()
                    .background("#F9F9F9".toColor())
                    .padding(innerPadding)
                    .verticalScroll(scrollState))
        }
    )
}