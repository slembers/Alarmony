package com.slembers.alarmony.feature.report

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.toColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun ReportListScreen(navController: NavController = rememberNavController()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("관리자 페이지") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = { 
            Text(text = "신고 목록 페이지")
            Button(
                onClick = { navController.navigate(NavItem.ReportDetail.route) },
                modifier = Modifier
                    .padding(5.dp)
                    .size(90.dp),
            ) {
                Text("디테일 페이지로 이동")
            }
        }
    )
}