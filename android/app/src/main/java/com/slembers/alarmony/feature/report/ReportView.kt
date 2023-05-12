package com.slembers.alarmony.feature.report

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha.disabled
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.slembers.alarmony.feature.ui.group.GroupBottomButtom
import com.slembers.alarmony.network.service.ReportService

data class RadioOption(val label: String, val value: String)

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun ReportScreen(navController: NavController = rememberNavController()) {
    val options = listOf(
        RadioOption("어플 기능 버그", "APP_BUG"),
        RadioOption("유저 신고", "USER_REPORT")
    )
    var selectedOption by remember { mutableStateOf(options[0]) }
    var smallText by remember { mutableStateOf("") }
    var bigText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("신고하기") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        bottomBar = {
            GroupBottomButtom(
                text = "신고 내역 전송",
                onClick = {
                    Log.d("INFO", "신고 전송하기")
                    ReportService.createReport(
                        reportType = selectedOption.value,
                        reportedNickname = smallText,
                        content = bigText
                    )
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    options.forEach { option ->
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = { selectedOption = option },
                            colors = RadioButtonDefaults.colors(MaterialTheme.colors.primary)
                        )
                        Text(
                            text = option.label,
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                }
                if(selectedOption.value == "USER_REPORT") {
                    Text(
                        text = " 신고 대상",
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(3.dp),
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 25.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal
                        ),
                    )
                    OutlinedTextField(
                        label = { Text("닉네임 입력") },
                        value = smallText,
                        onValueChange = { smallText = it },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Text(
                    text = "신고 내용",
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(3.dp),
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 25.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    )
                )
                TextField(
                    label = { Text("내용 입력") },
                    value = bigText,
                    onValueChange = { bigText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
        }
    )
}