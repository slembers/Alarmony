package com.slembers.alarmony.feature.common.ui.view


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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle

@Preview(showBackground = true)
@Composable
fun GroupTexteView() {
    Text(
        text = "123456789abcdefghijklmn",
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
            end = 0.dp
        ),
        textAlign = TextAlign.Start
    )
}

@Preview(showBackground = true)
@Composable
@ExperimentalMaterial3Api
fun GroupTitleView() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "그룹초대",
            style = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
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
        Icon(
            painter = painterResource(id = R.drawable.arrow_forward),
            contentDescription = null,
            modifier = Modifier.padding(2.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupSubjetView(

) {

    var text by remember{ mutableStateOf("") }

    Column(
        content = {
            GroupTitle(title = "그룹 제목") {}
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
    )
}

@Preview(showBackground = false)
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupCardView() {

    var text by remember{ mutableStateOf("") }

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
                    GroupTitle(title = "그룹 제목") {}
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
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupTimePickerView() {

//    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState()
//    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
//    val snackState = remember { SnackbarHostState() }
//    val showingPicker = remember { mutableStateOf(true) }
//    val snackScope = rememberCoroutineScope()
//    val configuration = LocalConfiguration.current

    Column{
        GroupTitle("알람시간") {}
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
}

@Preview(showBackground = true)
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupWeekView() {

    val width = remember { mutableStateOf(0.dp) }
    val weeks = remember { mutableListOf("월","화","수","목","금","토","일") }

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        width.value = this.maxWidth
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                start = 20.dp,
                top = 0.dp,
                bottom = 0.dp,
                end = 10.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)

        ) {
            items(weeks) {
                TextButton(
                    modifier = Modifier.size(width.value / 9),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    onClick = {},
                    content = {
                        Text(text = it)
                    }
                )
            }
        }
    }
}
