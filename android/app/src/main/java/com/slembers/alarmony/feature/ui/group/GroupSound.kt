package com.slembers.alarmony.feature.ui.group

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.GroupCard
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupSound(
    navController: NavController,
    sound: String?
) {
    /*
        GroupCard(
            title = {
                GroupTitle(
                    title = NavItem.Sound.title,
                    enable = true,
                    onClick = { navController.navigate(NavItem.Sound.route) },
                    content = {

                    }
                )
            }
        )
    */







    Row(
        modifier = Modifier
            .height(50.dp)
            .padding(5.dp)
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = { navController.navigate(NavItem.Sound.route) }
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(horizontalArrangement = Arrangement.Start) {
            Text(
                "노래",
                textAlign = TextAlign.Start

            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = sound ?: "노래제목",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 12.sp,
                    //fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal

                ),

                )
            Icon(
                painter = painterResource(id = R.drawable.arrow_forward),
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .size(12.dp)

            )
        }


    }


}