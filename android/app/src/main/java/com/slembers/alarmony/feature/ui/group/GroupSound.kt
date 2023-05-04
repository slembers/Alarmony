package com.slembers.alarmony.feature.ui.group

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
    navController : NavController,
    sound : String
) {
    GroupCard(
        title = { GroupTitle(
            title = NavItem.Sound.title,
            onClick = { navController.navigate( NavItem.Sound.route) },
            content = {
                Row(
                    modifier = Modifier.height(50.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sound,
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 15.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Normal
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_forward),
                        contentDescription = null,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
        )
        }
    )
}