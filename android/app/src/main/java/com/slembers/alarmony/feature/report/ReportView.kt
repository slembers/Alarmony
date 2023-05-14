package com.slembers.alarmony.feature.report

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.network.service.ReportService
import com.slembers.alarmony.util.showToast
import com.slembers.alarmony.viewModel.GroupSearchViewModel

data class RadioOption(val label: String, val value: String)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun ReportScreen(
    navController: NavController = rememberNavController(),
    viewModel: GroupSearchViewModel = viewModel(),
) {
    var reportType by remember { mutableStateOf(ReportType.USER_REPORT) }
    var reported by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val context = LocalContext.current
    var currentMember = viewModel.curMember.observeAsState()
    var found = viewModel.found.observeAsState()

    fun validParams() : Boolean {
        if(reportType == null) return false
        if(content.isNullOrEmpty()) {
            showToast(context,"내용을 입력하세요!")
            return false
        }
        if(reportType == ReportType.USER_REPORT){
            if(currentMember == null || currentMember.value?.nickname.isNullOrEmpty() || !found.value!!) {
                showToast(context,"신고 대상이 존재하지 않거나 유효하지 않습니다.")
                return false
            }
        }
        return true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("신고하기") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.background
            )
        },
        content = { innerPadding ->
            Column {
                Text(
                    text = "신고유형 선택",
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = reportType == ReportType.APP_BUG,
                        onClick = { reportType = ReportType.APP_BUG },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("어플 기능 버그")

                    RadioButton(
                        selected = reportType == ReportType.USER_REPORT,
                        onClick = { reportType = ReportType.USER_REPORT },
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    )
                    Text("유저 신고")
                }

                Divider(Modifier.padding(vertical = 8.dp, horizontal = 8.dp))

                if (reportType == ReportType.USER_REPORT) {
                    SearchSingleMember(viewModel = viewModel)

                    Divider(Modifier.padding(vertical = 8.dp, horizontal = 8.dp))
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    label = { androidx.compose.material3.Text("신고내용") },
                )
                Button(
                    onClick = {
                        if( validParams()){
                            ReportService.createReport(
                                reportType = reportType.name,
                                reportedNickname = currentMember.value?.nickname,
                                content = content,
                                context = context
                            )
                            viewModel.setFound(false)
                            navController.popBackStack()
                        }
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    androidx.compose.material3.Text("신고내용 전송")
                }
            }
        }
    )
}

enum class ReportType {
    APP_BUG,
    USER_REPORT
}
