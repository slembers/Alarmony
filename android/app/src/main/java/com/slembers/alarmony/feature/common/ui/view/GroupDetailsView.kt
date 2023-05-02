package com.slembers.alarmony.feature.common.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle


@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsTitleView() {
    CardBox(
        title = { CardTitle(title = "장덕모임") },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 0.dp,
                        bottom = 10.dp,
                        end = 10.dp
                    ),
                content = {
                    CardDividerView()
                    GroupDetailsRepeatView()
                }
            )
        }
    )
}

@Composable
fun CardDividerView() {
    Divider(
        thickness = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(2.dp)
            .background(
                color = Color(0xff9A9A9A),
                shape = MaterialTheme.shapes.extraSmall
            )
    )
}

@Composable
fun GroupDetailsTextView(
    text : String = "폰트",
    fontsize : TextUnit = 20.sp,
    color : Color = Color.Black
) {
    Text(
        text = text,
        style = TextStyle(
            color = color,
            fontSize = fontsize,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        textAlign = TextAlign.Start
    )
}

@Composable
fun GroupDetailsRepeatView() {

    val week = remember { mutableStateListOf("월","화","수","목","금","토","일",) }

    Column(
        modifier = Modifier.padding(
            start = 20.dp,
            top = 0.dp,
            bottom = 0.dp,
            end = 0.dp
        ),
        content = {
            GroupDetailsTextView("오전")
            GroupDetailsTextView(text = "07:30", fontsize = 50.sp)
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(week) {
                        GroupDetailsTextView(
                            text = it,
                            color = Color.Gray
                        )
                    }
                }
            )
        }
    )
}

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsBoardView() {
    CardBox(
        title = { CardTitle(
            title = "오늘 알람 기록",
            content =  {
                Text(
                    text = "23/04/19",
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal
                    ),
                    modifier = Modifier.padding(end = 10.dp),
                    textAlign = TextAlign.Start
                )
            }
        ) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 0.dp,
                        bottom = 10.dp,
                        end = 10.dp
                    ),
                content = {
                    CardDividerView()
                    MemberDetailsView( isCheck = true )
                    MemberDetailsView( isCheck = true )
                    MemberDetailsView( isCheck = true )
                    MemberDetailsView( isCheck = true )
                    CardDividerView()
                    MemberDetailsView()
                    MemberDetailsView()
                }
            )
        }
    )
}

@Preview
@Composable
fun GroupDetailsBoardBtnView(
    isCheck : Boolean = false,
    modifier : Modifier = Modifier
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            contentColor = AlarmisCheck(isCheck, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.background),
            containerColor = AlarmisCheck(isCheck, MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.primary)
        ),
        onClick = { /*TODO*/ },
        modifier = modifier,
        content = {
            Icon(
                imageVector = AlarmisCheck(isCheck, Icons.Default.Check, Icons.Default.Campaign),
                contentDescription = null
            )
            Text(
            text = AlarmisCheck(isCheck, "일어났어요", "알람보내기"),
                textDecoration = TextDecoration.Underline
            )
        }
    )
}

private fun <T> AlarmisCheck(isCheck : Boolean, True : T, False : T) : T {
    return isCheck.let { check ->
        when(check) {
            true -> True
            false -> False
        }
    }
}

@Preview
@Composable
fun AlarmCheckBtnView() {
    Column {
        GroupDetailsBoardBtnView(true)
        GroupDetailsBoardBtnView(false)
    }
}

@Preview
@Composable
fun MemberDetailsView(
    profile : String? = null,
    nickname : String = "Alarmony",
    isCheck : Boolean = false,
    onClick : () -> Unit = {}
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 2.dp, end = 20.dp, top = 3.dp, bottom = 1.dp)
            .fillMaxWidth()
            .wrapContentHeight()
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
            text = nickname,
            fontSize = 17.sp,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )
        Button(
            colors = ButtonDefaults.buttonColors(
                contentColor = AlarmisCheck(isCheck, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.background),
                containerColor = AlarmisCheck(isCheck, MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.primary)
            ),
            onClick = onClick,
            modifier = Modifier
                .width(150.dp)
                .padding(start = 0.dp, end = 0.dp),
            content = {
                Icon(
                    imageVector = AlarmisCheck(isCheck, Icons.Default.Check, Icons.Default.Campaign),
                    contentDescription = null
                )
                Text(
                    text = AlarmisCheck(isCheck, "일어났어요", "알람보내기"),
                    textDecoration = TextDecoration.Underline
                )
            }
        )
    }
}