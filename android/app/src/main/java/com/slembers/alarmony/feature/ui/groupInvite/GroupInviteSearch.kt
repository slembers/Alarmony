package com.slembers.alarmony.feature.ui.groupInvite

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.viewModel.GroupSearchViewModel
import com.slembers.alarmony.viewModel.GroupViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun SearchInviteMember(
    search: GroupSearchViewModel = viewModel(),
    currentMembers: MutableList<Member> = mutableListOf()
) {

    val keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done,
    //    capitalization = KeyboardCapitalization.Sentences
    )
    var text by remember { mutableStateOf("") }
    val searchMembers = search.searchMembers.observeAsState()
    val checkMembers by search.checkedMembers.observeAsState()

    CardBox(
        title = { CardTitle(title = "검색") },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 10.dp,
                        bottom = 10.dp,
                        end = 20.dp
                    )
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = {
                        text = it
                        search.searchApi(keyword = text)
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                    ),
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    keyboardOptions = keyboardOptions,
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xffD9D9D9),
                                    shape = MaterialTheme.shapes.extraLarge
                                )
                                .padding(horizontal = 16.dp), // inner padding
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Favorite icon",
                                tint = Color.DarkGray
                            )
                           // Spacer(modifier = Modifier.width(width = 8.dp))
                            Box(
                                modifier = Modifier
                                    .padding(start = 10.dp) // margin left and right
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp), // inner padding
                            ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = "닉네임을 입력해주세요.",
                                    modifier = Modifier.fillMaxWidth(1f),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.LightGray
                                )
                            }
                            innerTextField()
                        }
                        }
                    }

                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(searchMembers.value ?: mutableListOf()) {
                        val member = Member(
                            nickname = it.nickname,
                            profileImg = it.profileImg,
                            isNew = false
                        )
                        if(!checkMembers!!.contains(member) && !currentMembers.contains(member)) {
                            // 현재 인원에 포함되면 안됨 && 체크되어 있지 않아야함
                            SearchMember(
                                member = it,
                                onCheckedChange = {
                                    if (currentMembers.contains(it))
                                        search.removeCheckedMember(it)
                                    else
                                        search.addCheckedMember(it)
                                }
                            )   
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SearchMember(
    member : MemberDto = MemberDto(nickname = "임시유저", profileImg = null),
    onCheckedChange : (Member) -> Unit,
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 2.dp, end = 20.dp, top = 3.dp, bottom = 1.dp)
            .fillMaxWidth()
            .clickable {
                val checked = Member(
                    member.nickname,
                    member.profileImg,
                    false
                )
                onCheckedChange(checked)
            }
    )
    {
//        if(member.profileImg != null ) {
//            AsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(member.profileImg)
//                    .build(),
//                contentDescription = "ImageRequest example",
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .size(50.dp),
//                contentScale = ContentScale.Crop,
//                error = painterResource(id = R.drawable.baseline_account_circle_24)
//            )
//        }
//        else {
//            Image(
//                painter = painterResource(id = R.drawable.baseline_account_circle_24),
//                contentDescription = "ImageRequest example",
//                modifier = Modifier
//                    .clip(CircleShape)
//                    .size(65.dp)
//            )
//        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(member.profileImg)
                .build(),
            contentDescription = "ImageRequest example",
            modifier = Modifier
                .clip(CircleShape)
                .size(48.dp),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.baseline_account_circle_24)
        )
        Text(
            text = member.nickname,
            fontSize = 17.sp,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 10.dp)
        )
    }
}
