package com.slembers.alarmony.feature.user


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
        Text("로그인")
    }
}