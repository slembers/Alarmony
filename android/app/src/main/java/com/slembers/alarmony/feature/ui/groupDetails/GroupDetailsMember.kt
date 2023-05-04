package com.slembers.alarmony.feature.ui.groupDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.slembers.alarmony.R

@Preview
@Composable
fun MemberDetails(
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
        GroupDetailsBoardBtn(isCheck = isCheck)
    }
}