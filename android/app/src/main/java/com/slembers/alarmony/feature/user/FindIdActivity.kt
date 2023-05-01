package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slembers.alarmony.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Preview
@Composable
fun FindId() {

    var email = remember { mutableStateOf("") }
    var certnum = remember { mutableStateOf("") }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("아이디 찾기") },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
                ) {

                mascott(drawing = R.drawable.mascot_foreground)
                logo(drawing = R.drawable.alarmony)

                TextField(

                    value = email.value,
                    onValueChange = {email.value = it},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    label = {Text(text = "이메일")},
                    modifier = Modifier
                        .fillMaxWidth()
                    )
                TextField(

                    value = certnum.value,
                    onValueChange = {certnum.value = it},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    label = {Text(text = "인증번호")},
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Button(
                    onClick = {
                    Log.d("확인", "아이디 찾기")
                }, modifier = Modifier
//                        .fillMaxWidth()
                )

                {
                     Text(text = "아이디 찾기")
                }
            }
        }


        )

}

