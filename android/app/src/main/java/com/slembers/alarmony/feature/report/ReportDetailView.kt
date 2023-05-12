package com.slembers.alarmony.feature.report

import android.annotation.SuppressLint
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun ReportDetailScreen(navController: NavController = rememberNavController()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("신고 상세 페이지") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = {
            Text(text = "신고 상세 페이지")
        }
    )
}