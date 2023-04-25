package com.slembers.alarmony.feature.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.slembers.alarmony.R

@Preview
@Composable
 @ExperimentalMaterial3Api
fun groupToolBar() {
    TopAppBar(
        title = { Text(text = "그룹 생성")},
        modifier = Modifier.border(
            BorderStroke(
                1.dp, Color.Black
            )
        ),
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back_ios),
                contentDescription = null
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors()
    )
}