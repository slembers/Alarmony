package com.slembers.alarmony.feature.user

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
//import com.example.login.extra


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{


            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
//                    Image(painter = painterResource(id = R.drawable.alarmony),
//                        contentDescription = "sd",
//                            modifier = Modifier
//                            .height(200.dp)
//                        .fillMaxWidth()
//                        .padding(top = 16.dp, bottom = 16.dp))

                SignupForm()
                extra()

            }
        }
    }
}


@Preview
@Composable
fun SignupForm() {
    extra()
}


@Preview
@Composable
fun SignupNickname() {
    val Nickname = remember { mutableStateOf("") }

    TextField(
        value = Nickname.value, onValueChange = { Nickname.value = it },
        label = { Text("Nickname") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier


    )
}




@Preview
@Composable
fun SignuEmail() {
    val Email = remember { mutableStateOf("") }
    MaterialTheme {
        TextField(value = Email.value, onValueChange = {Email.value = it},
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Red,
                unfocusedIndicatorColor = Color.Blue
            ),
            modifier = Modifier
        )
    }
}

@Composable
fun Passwordcnf() {
    val Passwordcnf = remember {
        mutableStateOf("")}
    TextField(
        value = Passwordcnf.value, onValueChange = { Passwordcnf.value = it },
        label = { Text("Passwordcnf") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier


    )
}


@Preview
@Composable
fun SignupId() {
    val ID = remember { mutableStateOf("") }
    TextField(value = ID.value, onValueChange = {ID.value = it},
        label = { Text("ID") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier


    )

}