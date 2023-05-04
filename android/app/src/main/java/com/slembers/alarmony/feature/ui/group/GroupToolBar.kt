package com.slembers.alarmony.feature.ui.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.NavItem

@Preview
@Composable
 @ExperimentalMaterial3Api
fun GroupToolBar(
    title: String = "페이지 제목",
    navcontroller: NavHostController = rememberNavController()
) {
    TopAppBar(
        title = { Text(text = title)},
        modifier = Modifier
            .border(BorderStroke(1.dp, Color.Black))
            .padding(start = 5.dp),
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                contentDescription = null,
                modifier = Modifier.clickable { navcontroller.popBackStack() }
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors()
    )
}