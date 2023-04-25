package com.slembers.alarmony.compose.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun groupTitle() {
    var text by remember { mutableStateOf("") }

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
            Column {
                Text(text = "그룹이름",
                    color = Color.Black,
                    modifier = Modifier.padding(start = 20.dp, top = 0.dp, bottom = 0.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, bottom = 5.dp, end = 20.dp)
                        .border(
                            BorderStroke(
                                0.dp,
                                SolidColor(MaterialTheme.colorScheme.background)
                            )
                        )
                        ,
                    singleLine = true
                )
            }
        }
    )
}