package com.slembers.alarmony.feature.ui.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slembers.alarmony.feature.common.ui.theme.toColor
import kotlinx.serialization.json.JsonNull.content


@Preview
@Composable
@ExperimentalMaterial3Api
fun GroupBottomButtom(
    text : String = "버튼 이름",
    onClick : () -> Unit = {}
) {
    BottomAppBar(
        modifier = Modifier.height(50.dp),
        contentPadding = PaddingValues(0.dp),
        content = {
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        "#00B4D8".toColor(),
                        MaterialTheme.shapes.extraSmall
                    ),
                onClick = onClick,
                content = {
                    Text(
                        text = text,
                        color = Color.Black,
                    )
                }
            )
        }
    )
}