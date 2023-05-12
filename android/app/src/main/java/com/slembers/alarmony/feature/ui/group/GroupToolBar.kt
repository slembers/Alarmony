package com.slembers.alarmony.feature.ui.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Colors
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.notosanskr
import com.slembers.alarmony.feature.common.ui.theme.toColor

@Preview
@Composable
 @ExperimentalMaterial3Api
fun GroupToolBar(
    title: String = "페이지 제목",
    navClick: () -> Unit = {},
    action: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        title = { Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontFamily = notosanskr,
                modifier = Modifier.padding(5.dp)
            )
        },
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp)
            .shadow(
                elevation = 10.dp,
                ambientColor = Color.Blue,
                spotColor = Color.Gray,
            ),
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                modifier = Modifier.clickable(onClick = navClick)
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = "#FFFFFF".toColor()),
        actions = action

    )
}