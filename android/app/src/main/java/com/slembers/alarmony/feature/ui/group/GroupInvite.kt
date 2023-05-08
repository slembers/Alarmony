package com.slembers.alarmony.feature.ui.group

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.GroupCard
import com.slembers.alarmony.feature.common.ui.compose.GroupDefalutProfile
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.network.service.MemberService

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupInvite(
    navController : NavHostController,
    members : List<MemberDto> = listOf()
) {
    GroupCard(
        title = { GroupTitle(
            title = NavItem.GroupInvite.title,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward),
                    contentDescription = null,
                    modifier = Modifier.padding(2.dp)
                )
            },
            onClick = {
                MemberService.login()
                navController.currentBackStackEntry?.savedStateHandle?.set("members",members.toSet())
                navController.navigate( route = NavItem.GroupInvite.route )
            }
        )
        },
        content = {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            top = 0.dp,
                            bottom = 0.dp,
                            end = 10.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    userScrollEnabled = true

                ) {
                    items(members) { item ->
                        GroupDefalutProfile(item.nickname)
                    }
                }
            }
        }
    )
}