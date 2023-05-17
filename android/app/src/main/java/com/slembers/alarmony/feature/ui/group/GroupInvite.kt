package com.slembers.alarmony.feature.ui.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.GroupCard
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.feature.ui.groupInvite.GroupDefalutProfile
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.viewModel.GroupViewModel

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupInvite(
    navController : NavHostController,
    members : MutableList<Member> = mutableListOf(),
    viewModel : GroupViewModel = viewModel()
) {
    GroupCard(
        title = { GroupTitle(
            title = NavItem.GroupInvite.title,
            enable = true,
            content = {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_forward),
                    contentDescription = null,
                    modifier = Modifier.padding(2.dp)
                )
            },
            onClick = { navController.navigate( route = NavItem.GroupInvite.route ) }
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
                        GroupInviteMember(
                            member = item,
                            cancel = {
                                viewModel.removeMember(it)
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupInviteMember(
    member : Member? = Member(),
    cancel : (Member) -> Unit = {}
) {
    BoxWithConstraints(
        modifier = Modifier
            .width(60.dp)
            .height(70.dp)
    ) {
        val maxWidth = this.maxWidth
        Column(
            modifier = Modifier
                .width(maxWidth)
                .fillMaxHeight()
                .height(this.maxHeight)
        ) {
            Box(
                modifier = Modifier
                    .width(maxWidth)
                    .weight(1f)
                    .padding(0.dp),
                content = {
                    if(member?.profileImg == null) {
                        Image(
                            modifier = Modifier
                                .matchParentSize()
                                .align(Alignment.Center),
                            painter = painterResource(id = R.drawable.baseline_account_circle_24),
                            contentDescription = null)
                    } else {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .align(Alignment.Center),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(member.profileImg)
                                .build(),
                            contentDescription = member.nickname,
                            contentScale = ContentScale.Crop,
                            error = painterResource(id = R.drawable.baseline_account_circle_24)
                        )
                    }
                    if(member!!.isNew) {
                        Box(
                            modifier = Modifier
                                .size(maxWidth / 2)
                                .align(Alignment.BottomEnd)
                        ) {
                            IconButton(
                                modifier = Modifier
                                    .size(maxWidth / 4)
                                    .align(Alignment.Center),
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = Color.Red,
                                    containerColor = MaterialTheme.colorScheme.error
                                ),
                                content = { Icon(
                                    imageVector = Icons.Filled.Remove,
                                    contentDescription = "그룹퇴출")
                                },
                                onClick = { cancel(member) }
                            )
                        }
                    }
                }
            )
            Text(
                text = member!!.nickname,
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                textAlign = TextAlign.Center,
                fontSize = 10.sp
            )
        }
    }
}