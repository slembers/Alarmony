package com.slembers.alarmony.feature.report

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.viewModel.GroupSearchViewModel

@Preview
@OptIn(ExperimentalComposeUiApi::class)
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun SearchSingleMember(
    viewModel: GroupSearchViewModel = viewModel(),
    currentMembers: MutableList<Member> = mutableListOf(),
) {

    var text by remember { mutableStateOf("") }
    val search: GroupSearchViewModel = viewModel()
    val searchMembers = search.searchMembers.observeAsState()
//    var currentMember = search.curMember.observeAsState()
    var found = search.found.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                viewModel.setFound(false)
                text = it
                search.searchApi(keyword = text)
            },
            label = { Text("신고자 검색") },
            modifier = Modifier
                .fillMaxWidth()
        )
        if (!found.value!!) {
            LazyColumn() {
                items(searchMembers.value ?: mutableListOf()) {
                    // 현재 인원에 포함되면 안됨
                    SearchOneMember(
                        member = it,
                        whenCheck = { it->
                            viewModel.selectTarget(it)
                            text = it.nickname
                            viewModel.setFound(true)
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun SearchOneMember(
    member : MemberDto = MemberDto(nickname = "임시유저", profileImg = null),
    whenCheck : (Member) -> Unit
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
                    member.profileImg
                )
                whenCheck(checked)
            }
    )
    {
        if (member.profileImg != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(member.profileImg)
                    .build(),
                contentDescription = "ImageRequest example",
                modifier = Modifier
                    .size(65.dp)
                    .clip(shape = CircleShape)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.baseline_account_circle_48),
                contentDescription = "ImageRequest example",
                modifier = Modifier
                    .size(65.dp)
                    .clip(shape = CircleShape)
            )
        }
        Text(
            text = member.nickname,
            fontSize = 17.sp,
            modifier = Modifier.fillMaxWidth(1f)
        )
    }
}
