package com.slembers.alarmony.feature.ui.group

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    enabled : Boolean = false,
    onClick : () -> Unit = {}
) {
    Box(
      //  modifier = Modifier.height(50.dp),
       // contentPadding = PaddingValues(0.dp),

        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier.fillMaxWidth()
    )
        {
            Button(
                colors = ButtonDefaults.buttonColors(
                     containerColor = "#7DC3F2".toColor(),
                     contentColor = Color.Black
                ),
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = onClick,
                enabled = enabled,
                content = {
                    Text(text = text)
                }
            )
        }

}