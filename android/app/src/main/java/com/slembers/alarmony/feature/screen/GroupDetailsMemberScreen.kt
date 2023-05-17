package com.slembers.alarmony.feature.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardDivider
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.viewModel.GroupDetailsViewModel

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsMemberScreen(
    navController : NavHostController = rememberNavController(),
    details : GroupDetailsViewModel = viewModel(),
    alarmId: Long? = null
) {
    Log.d("통계","진입했습니다.")
    var currentMembers by remember { mutableStateOf<MutableList<Member>>(mutableListOf()) }

    LaunchedEffect(Unit) {
        val _member = details.updateCurrentMember(alarmId!!)
        currentMembers = _member
    }

    Scaffold(
        topBar = {
            GroupToolBar(
                //title = NavItem.GroupDetails.title, 수정[1]
                title = NavItem.GroupDetailsMembers.title,
                navClick = { navController.popBackStack() }
            )
        },
        containerColor = "#F9F9F9".toColor(),
        content = { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding).padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    CardBox(
                        title = { CardTitle(title = "그룹통계") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(5.dp)
                            .padding(4.dp)
                            .shadow(
                                elevation = 5.dp,
                                ambientColor = Color.Black,
                                spotColor = Color.Black,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        content = {
                            CardDivider(color = Color(0xff9A9A9A))
                            LazyColumn {
                                items(currentMembers) {
                                    // 현재 인원에 포함되면 안됨
                                    GroupMembers(it)
                                }
                            }
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun GroupMembers(
    member : Member = Member()
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 2.dp, end = 20.dp, top = 3.dp, bottom = 1.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .heightIn(
                minOf(50.dp),
                maxOf(80.dp)
            )
    )
    {
        if(member.profileImg != null ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(member.profileImg)
                    .build(),
                contentDescription = "ImageRequest example",
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.account_circle),
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.baseline_account_circle_48),
                contentDescription = "ImageRequest example",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = member.nickname,
                fontSize = 17.sp,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

    }
}