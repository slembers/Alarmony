package com.slembers.alarmony.feature.ui.groupInvite

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.network.service.GroupService
import com.slembers.alarmony.viewModel.GroupSearchViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.json.JsonNull.content

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun CurrentInvite(
    search : GroupSearchViewModel = viewModel(),
    currentMembers: MutableList<Member> = mutableListOf(),
) {

    val checkMembers = search.checkedMembers.observeAsState()

    CardBox(
        title = { CardTitle(title = "초대인원") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .heightIn(
                min(100.dp, 100.dp)
            )
            .border(
                BorderStroke(0.dp, MaterialTheme.colorScheme.background),
                MaterialTheme.shapes.medium
            )
            .padding(4.dp)
//            .border(1.dp, Color.Black, MaterialTheme.shapes.medium)
            .shadow(
                elevation = 5.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                shape = RoundedCornerShape(20.dp)
            ),
        content = {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 5.dp,
                        bottom = 10.dp,
                        end = 0.dp
                    ),
                userScrollEnabled = true
            ) {

                items(items = currentMembers) { checked ->
                    GroupDefalutProfile(
                        member = checked
                    )
                }

                items(items = checkMembers.value ?: listOf()) { checked ->
                    GroupDefalutProfile(
                        member = checked,
                        cancel = { search.removeCheckedMember(it) }
                    )
                }
            }
        }
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDefalutProfile(
    member : Member? = Member(),
    cancel : (Member) -> Unit = {}
) {
    BoxWithConstraints(
        modifier = Modifier
            .width(48.dp)
            .height(60.dp)
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
//                    if(member?.profileImg == null) {
//                        Image(
//                            modifier = Modifier
//                                .matchParentSize()
//                                .align(Alignment.Center),
//                            painter = painterResource(id = R.drawable.baseline_account_circle_24),
//                            contentDescription = null)
//                    } else {
//                        AsyncImage(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .clip(CircleShape)
//                                .align(Alignment.Center),
//                            model = ImageRequest.Builder(LocalContext.current)
//                                .data(member.profileImg)
//                                .build(),
//                            contentDescription = member.nickname,
//                            contentScale = ContentScale.Crop,
//                            error = painterResource(id = R.drawable.baseline_account_circle_24)
//                        )
//                    }
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(member?.profileImg)
                            .build(),
                        contentDescription = member?.nickname,
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.baseline_account_circle_24)
                    )
                    if(!member!!.isNew) {
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