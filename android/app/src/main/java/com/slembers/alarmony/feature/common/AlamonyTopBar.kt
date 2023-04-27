package com.slembers.alarmony.feature.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.slembers.alarmony.R

@Composable
@ExperimentalMaterial3Api
fun AlamonyTopBar(
    title : String = "제목"
) {
    TopAppBar(
        title = { Text(text = title) },
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
