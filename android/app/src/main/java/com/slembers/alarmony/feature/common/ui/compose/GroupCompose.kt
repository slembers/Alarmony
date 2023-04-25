package com.slembers.alarmony.feature.common.ui.compose

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import java.util.Locale

@Composable
@ExperimentalMaterial3Api
fun GroupText(
    title : String
) {
    Text(
        text = title,
        style = TextStyle(
            color = Color.Black,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        modifier = Modifier.padding(
            start = 20.dp,
            top = 0.dp,
            bottom = 0.dp,
            end = 0.dp),
        textAlign = TextAlign.Start
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupTitle(
    title : String,
    icon : @Composable() () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal
            ),
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    top = 0.dp,
                    bottom = 0.dp,
                    end = 0.dp
                )
                .weight(1f),
            textAlign = TextAlign.Start
        )
        icon()
    }
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupSubjet(
) {

    var text by remember{ mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 20.sp,
            fontFamily = FontFamily.Monospace,
            fontStyle = FontStyle.Normal
        ),
        modifier = Modifier
            .padding(
                start = 20.dp,
                top = 0.dp,
                bottom = 0.dp,
                end = 10.dp
            )
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.background),
                MaterialTheme.shapes.extraSmall
            ),
        singleLine = true
    )
    Divider(
        color = Color.Black,
        thickness = 1.dp,
        modifier = Modifier
            .padding(
                start = 20.dp,
                top = 0.dp,
                bottom = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth()
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupCard(
    title : @Composable() () -> Unit,
    content: @Composable() () -> Unit
) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(
                BorderStroke(0.dp, MaterialTheme.colorScheme.background),
                MaterialTheme.shapes.medium
            )
            .padding(4.dp)
            .border(1.dp, Color.Black, MaterialTheme.shapes.medium)
            .shadow(
                elevation = 1.dp,
                MaterialTheme.shapes.medium,
                ambientColor = Color.Gray
            ),
        content = {
            Column(
                content = {
                    title()
                    content()
                }
            )
        }
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupTimePicker() {

    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState()
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val snackState = remember { SnackbarHostState() }
    val showingPicker = remember { mutableStateOf(true) }
    val snackScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current

    TimeInput(
        state = state,
        modifier = Modifier
            .padding(
                start = 20.dp,
                top = 10.dp,
                bottom = 0.dp,
                end = 0.dp)
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupWeeks() {

    var isCheck = remember { mutableMapOf<String,Boolean>() }
    val width = remember { mutableStateOf(0.dp) }
    val weeks = listOf<String>("월","화","수","목","금","토","일")

    for(week in weeks) {
        isCheck[week] = true
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 5.dp,
                bottom = 5.dp,
                end = 10.dp
            )
    ) {
        width.value = this.maxWidth
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)

        ) {
            items(weeks) {
                TextButton(
                    modifier = Modifier.size(width.value / 8),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor =
                        if (isCheck[it] == true) {
                            MaterialTheme.colorScheme.primary
                        }
                        else
                            MaterialTheme.colorScheme.background
                    ),
                    onClick = {
                        isCheck[it] = if(isCheck[it] == true) false else true
                        Log.d("click event","isCheck value : ${isCheck[it]}")
                    },
                    content = {
                        Text(text = it)
                    }
                )
            }
        }
    }
}
