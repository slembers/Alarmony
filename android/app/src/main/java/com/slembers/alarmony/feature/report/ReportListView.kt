package com.slembers.alarmony.feature.report

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.model.db.dto.ReportListDto
import com.slembers.alarmony.network.service.ReportService

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun ReportListScreen(navController: NavController = rememberNavController()) {
    val reportList = remember { mutableStateOf<ReportListDto?>(null) }

    LaunchedEffect(Unit) {
        try {
            reportList.value = ReportService.getReportList()
        } catch (e: Exception) {
            Log.e("ReportListScreen", "Failed to get report list", e)
        }
    }
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
        }
    ) {
        Column {
            Text(
                text = "신고 목록 페이지",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Row {
                Text(
                    text = "신고 구분",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "신고자",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "피신고자",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }
            reportList.value?.reports?.forEach { item ->
                Row(
                    modifier = Modifier.clickable {
                        navController.navigate("${NavItem.ReportDetail.route}/${item.reportId}")
                    }
                ) {
                    Text(
                        text = item.reportType ?: "정보없음",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = item.reporterNickname ?: "정보없음",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = item.reportedNickname ?: "정보없음",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )

                }
            }
        }
    }
}