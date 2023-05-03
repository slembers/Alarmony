package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.slembers.alarmony.R

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")

@Composable
fun Findpswd(navController: NavController) {


    var email = remember { mutableStateOf("") }
    var ID = remember { mutableStateOf("") }
    var certnum = remember { mutableStateOf("") }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("비밀번호 찾기") },
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp() }) {
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

                    value = ID.value,
                    onValueChange = {ID.value = it},
                    label = {Text(text = "아이디")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                TextField(

                    value = email.value,
                    onValueChange = {email.value = it},
                    label = {Text(text = "이메일")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextField(

                    value = certnum.value,
                    onValueChange = {certnum.value = it},
                    label = {Text(text = "인증번호")},
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                Button(
                    onClick = {
                        Log.d("확인", "비밀번호 찾기")
                    }, modifier = Modifier
//                        .fillMaxWidth()
                )

                {
                    Text(text = "비밀번호 찾기")
                }
            }
        }

    )

}

