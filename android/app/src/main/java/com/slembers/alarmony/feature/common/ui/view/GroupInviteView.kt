package com.slembers.alarmony.feature.common.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Checkbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle

@Preview(showBackground = true)
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDefalutProfileView(
    image : Painter = painterResource(id = R.drawable.baseline_account_circle_24),
    nickname : String = "nothing",
    newMember : Boolean = true
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
                    Image(
                        modifier = Modifier
                            .matchParentSize()
                            .align(Alignment.Center),
                        painter = image,
                        contentDescription = null)
                    if(newMember) {
                        Box(
                            modifier = Modifier
                                .size(maxWidth / 2)
                                .align(Alignment.BottomEnd)
                                .padding(5.dp)
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .size(maxWidth / 4)
                                    .align(Alignment.Center),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.Black,
                                    containerColor = MaterialTheme.colorScheme.error
                                ),
                                content = { Text(
                                    text = "x",
                                    color = Color.Black
                                )},
                                onClick = { /*TODO*/ }
                            )
                        }
                    }
                }
            )
            Text(
                text = nickname,
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                textAlign = TextAlign.Center,
                fontSize = 10.sp
            )
        }
    }
}


@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun CurrentInviteView() {
    CardBox(
        title = { CardTitle(title = "초대인원") },
        content = {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 0.dp,
                        bottom = 0.dp,
                        end = 0.dp
                    ),
                userScrollEnabled = true
            ) {
                items( count = 8 ) {
                    GroupDefalutProfileView(
                        image = painterResource(id = R.drawable.account_circle),
                        nickname = "ssafy01",
                        newMember = true
                    )
                }
            }
        }
    )
}

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun SearchInviteMemberView() {

    var text by remember{ mutableStateOf("") }

    CardBox(
        title = { CardTitle(title = "검색") },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 0.dp,
                        bottom = 10.dp,
                        end = 20.dp
                    )
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = {text = it},
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    ),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
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
                            Spacer(modifier = Modifier.width(width = 8.dp))
                            if (text.isEmpty()) {
                                Text(
                                    text = "닉네임을 입력새주세요.",
                                    modifier = Modifier.fillMaxWidth(1f),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.LightGray
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                LazyColumn() {
                    items(count = 10) {
                        SearchMemberView()
                    }
                }
            }
        }
    )
}

@Preview
@Composable
fun SearchMemberView() {

    var isClicked by remember { mutableStateOf(false)  }
    val profile by remember { mutableStateOf(null) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 2.dp, end = 20.dp, top = 3.dp, bottom = 1.dp)
            .fillMaxWidth()
            .clickable { isClicked = !isClicked}
    )
    {
        if(profile != null ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profile)
                    .build(),
                contentDescription = "ImageRequest example",
                modifier = Modifier.size(65.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                contentDescription = "ImageRequest example",
                modifier = Modifier.size(65.dp)
            )
        }
        Text(
            text = "SSAFY에 오신걸 환영합니다.",
            fontSize = 17.sp,
            modifier = Modifier.fillMaxWidth(1f)
        )
        Checkbox(
            checked = isClicked,
            onCheckedChange = { isClicked = it }
        )
    }
}
