package com.slembers.alarmony.feature.user


import android.annotation.SuppressLint
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Switch
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.slembers.alarmony.feature.common.ui.theme.toColor

//import androidx.compose.ui.graphics.Color


class SettingView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BtnFm()


        }

    }
}

@Preview
@Composable
fun BtnFm() {
    Button(
        onClick = { /* 로그인 버튼 클릭 시 처리 */ },
        modifier = Modifier
            .width(100.dp)
            .padding(16.dp),

        ) {
        Text("로그인하하 임포트됨")
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")

@Composable
fun AccountMtnc(navController: NavController) {

    var Notichecked = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {/* 계정설정 창으로 가는 로직*/},
                modifier = Modifier
                    .width(240.dp)
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "계정 관리",
//            tint = LocalContentColor.current.copy(alpha = ContentAlpha.high)
                    )
                    Text(text = "계정 관리")
                }

            }
            Button(
                onClick = {/* 계정설정 창으로 가는 로직*/},
                modifier = Modifier
                    .width(240.dp)
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.OtherHouses,
                        contentDescription = "테마 설정",
//            tint = LocalContentColor.current.copy(alpha = ContentAlpha.high)
                    )
                    Text(text = "테마 설정")
                }

            }
            Button(
                onClick = {/* 계정설정 창으로 가는 로직*/},
                modifier = Modifier
                    .width(240.dp)
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "어플리케이션 정보",
//            tint = LocalContentColor.current.copy(alpha = ContentAlpha.high)
                    )
                    Text(text = "어플리케이션 정보")
                }

            }
            Button(
                onClick = {/* 계정설정 창으로 가는 로직*/},
                modifier = Modifier
                    .width(240.dp)
                    .padding(10.dp)
            ) {  Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "푸쉬알림 설정",
//            tint = LocalContentColor.current.copy(alpha = ContentAlpha.high)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "푸쉬알림 설정",
//            textAlign = TextAlign.Center

                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = Notichecked.value,
                    onCheckedChange = { /**/},
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
            }

        }

    }

}