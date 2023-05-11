package com.slembers.alarmony.feature.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.slembers.alarmony.feature.common.ui.theme.toColor


@Preview
@Composable
fun CommonDialog(
    title: String = "임시제목",
    context: String = "여기에 문구를 작성해주세요.",
    isClosed : MutableState<Boolean> = mutableStateOf(false),
    openDialog: MutableState<Boolean> = mutableStateOf(true),
    accept: () -> Unit = {},
    isButton: Boolean = true
) {
    if (openDialog.value) {
        AlertDialog(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.heightIn(
                min(100.dp,100.dp)
            ),
            onDismissRequest = {
                isClosed.value = false
            },
            title = {
                Column() {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp
                    )
                    Divider(
                        modifier = Modifier
                            .alpha(0.3f)
                            .padding(top = 10.dp, bottom = 5.dp, start = 5.dp, end = 5.dp)
                            .clip(shape = RoundedCornerShape(10.dp)),
                        thickness = 2.dp,
                        color = Color.Gray)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        text = context,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center)
                }
            },
            buttons = {
                if (isButton) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Button(
                            onClick = { isClosed.value = false },
                            colors = ButtonDefaults.buttonColors(containerColor = "#C93636".toColor())
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = "Notification",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                            Text(text = "취소")
                        }
                        Button(
                            onClick = accept,
                            colors = ButtonDefaults.buttonColors(containerColor = "#31AF91".toColor()),
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = "Notification",
                                tint = Color.White,
                                modifier = Modifier.size(25.dp)
                            )
                            Text("수락")
                        }
                    }
                }
            }
        )
    }
}