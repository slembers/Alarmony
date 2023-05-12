package com.slembers.alarmony.feature.report

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.slembers.alarmony.model.db.dto.ReportDto
import com.slembers.alarmony.network.service.ReportService

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun ReportDetailScreen(
    navController: NavController = rememberNavController(),
    reportId: Long? = null
) {
    val reportDetail = remember { mutableStateOf<ReportDto?>(null) }

    LaunchedEffect(Unit) {
        try {
            reportDetail.value = ReportService.getReportDetail(reportId!!)
        } catch (e: Exception) {
            Log.e("ReportDetailScreen", "Failed to get report detail", e)
        }
    }

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
            Column {
                Text(
                    text = "${reportDetail.value?.reportId}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = reportDetail.value?.reportType ?: "정보없음",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Row() {
                    Text(
                        text = reportDetail.value?.reporterNickname ?: "정보없음",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = reportDetail.value?.reportedNickname ?: "정보없음",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Text(
                    text = reportDetail.value?.content ?: "정보없음",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

        }
    )
}