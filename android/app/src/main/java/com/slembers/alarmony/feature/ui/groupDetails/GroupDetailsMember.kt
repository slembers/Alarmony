package com.slembers.alarmony.feature.ui.groupDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.slembers.alarmony.R
import com.slembers.alarmony.network.service.GroupService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.serialization.json.JsonNull.content

@Preview
@Composable
fun MemberDetails(
    profile : String? = null,
    nickname : String = "Alarmony",
    message : String = "체크하지 않았어요...",
    isCheck : Boolean = false,
    host : Boolean = false,
    alarmId : Long = 0
) {

    var onAlarm by remember { mutableStateOf( true ) }

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
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .padding(0.dp),
            content = {
                if(profile != null ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(profile)
                            .build(),
                        contentDescription = "ImageRequest example",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.baseline_account_circle_48)
                    )
                }
                else {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_account_circle_48),
                        contentDescription = "ImageRequest example",
                        modifier = Modifier
                            .clip(CircleShape)
                            .align(Alignment.Center)
                            .fillMaxSize()
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = nickname,
                fontSize = 17.sp,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
       /*     Text(
                text = message,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )*/
        }
        if(host && onAlarm && !isCheck) {
            GroupDetailsBoardBtn(
                isCheck = false,
                onClick = {
                    onAlarm = false
                    CoroutineScope(Dispatchers.IO).async {
                        GroupService.notification(
                            alarmId,
                            nickname
                        )
                    }
                }
            )
        } else if(!host && isCheck){
            GroupDetailsBoardBtn(
                isCheck = true,
            )
        }
    }
}