package com.slembers.alarmony.feature.alarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.slembers.alarmony.feature.common.ui.theme.notosanskr
import com.slembers.alarmony.feature.common.ui.theme.toColor

class NotificationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotificationScaffold()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScaffold() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBackIos,
                                contentDescription = "Notification",
                                tint = Color.Black,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Text(text = "알림",
                            fontWeight = FontWeight.Bold,
                            fontFamily = notosanskr,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                },
                modifier = Modifier.shadow(
                    elevation = 10.dp,
                    ambientColor = Color.Blue,
                    spotColor = Color.Gray,

                    ),
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = "#FFFFFF".toColor()
                ),
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .padding(innerPadding)
            ) {
                val itemArray = notiSample
                LazyColumn{
                    items(itemArray.size) {model ->
                        MyNotiItem(item = itemArray[model])
                    }
                }
            }
        },
        containerColor = "#F9F9F9".toColor()
    )
}

@Composable
fun MyNotiItem(item : Noti) {
    val isClicked = remember { mutableStateOf(false)  }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(70.dp)
            .shadow(
                elevation = 5.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                shape = RoundedCornerShape(50.dp)
            )
            .background(Color.White)
            .fillMaxWidth()
            .clickable {
                if (item.type) {
                    isClicked.value = true
                }
            },
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = "#FFFFFF".toColor()))

    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 2.dp, end = 20.dp, top = 3.dp, bottom = 1.dp)
                .fillMaxWidth()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.profile_img)
                    .build(),
                contentDescription = "ImageRequest example",
                modifier = Modifier.size(65.dp)
            )
            Text(
                text = item.content,
                fontSize = 17.sp
            )
        }
        if (isClicked.value) {
            GroupNoti(item, isClicked)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun NotificationPreview() {
    NotificationScaffold()
}

@Composable
fun GroupNoti(item : Noti, isClicked : MutableState<Boolean>) {
    val openDialog = remember { mutableStateOf(true)  }
    if (openDialog.value) {
        AlertDialog(
            shape = RoundedCornerShape(20.dp),
            onDismissRequest = {
                openDialog.value = false
                isClicked.value = false
            },
            title = {
                Column() {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "그룹초대",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp
                    )
                    Divider(
                        modifier = Modifier
                            .alpha(0.3f)
                            .padding(top = 10.dp, bottom = 5.dp, start = 5.dp, end = 5.dp)
                            .clip(shape = RoundedCornerShape(10.dp)),
                        thickness = 2.dp,
                        color = Color.Gray)
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Gray,
                        text = item.content.substring(0, item.content.length-10) + "에서\n 온 초대입니다.",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center)
                }
            },
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.padding(20.dp).fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            openDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = "#C93636".toColor())
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Notification",
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                        Text(text = "거절")
                    }
                    Button(
                        onClick = {
                            openDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = "#31AF91".toColor()),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Notification",
                            tint = Color.White,
                            modifier = Modifier.size(25.dp)
                        )
                        Text("수락")
                    }

                }
            }
        )
    }
}


