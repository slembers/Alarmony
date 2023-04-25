package com.slembers.alarmony.feature.group

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@ExperimentalMaterial3Api
fun GroupText(
    title : String
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 0.dp, bottom = 0.dp, end = 0.dp),
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = Color.Black
    )
}