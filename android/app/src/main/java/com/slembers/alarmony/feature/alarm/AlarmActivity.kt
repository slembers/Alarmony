package com.slembers.alarmony.feature.alarm

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.alarm.ui.theme.AlarmonyTheme
import com.slembers.alarmony.feature.alarm.ui.theme.notosanskr
import com.slembers.alarmony.feature.group.GroupScaffold

class AlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScaffold()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.mas),
                            contentDescription = "character",
                            modifier = Modifier.size(30.dp)
                        )
                        Text(text = "alarmony",
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
                actions = {
                    if (notiSample.isEmpty()) {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notification",
                                tint = Color.Black,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    } else {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Filled.Notifications,
                                contentDescription = "Notification_Active",
                                tint = Color.Red,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Setting",
                            tint = Color.Black,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                },
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* fab click handler */ },
                shape = CircleShape,
                containerColor = "#00B4D8".toColor(),
                modifier = Modifier
                    .offset(y = -50.dp)
                    .size(60.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Alarm Add",
                    modifier = Modifier
                        .size(40.dp),

                    tint = Color.White

                )
                contentColorFor(backgroundColor = Color.Gray)
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .padding(innerPadding)
            ) {
                val context = LocalContext.current
                val onListItemClick = { text : String ->
                    Toast.makeText(
                        context,
                        text,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                val itemArray = aaa
                LazyColumn{
                    items(itemArray.size) {model ->
                        MyListItem(item = itemArray[model], onItemClick=onListItemClick)
                    }
                }
            }
        },
        containerColor = "#F9F9F9".toColor()
    )
}

@Composable
fun MyListItem(item : Alarm, onItemClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(100.dp)
            .shadow(
                elevation = 5.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                shape = RoundedCornerShape(20.dp)
            )
            .background(Color.White)
            .fillMaxWidth()
            .clickable { onItemClick("Heelo") },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = "#FFFFFF".toColor()))

    {
        Column(verticalArrangement = Arrangement.Center) {

            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 25.dp, top = 5.dp, bottom = 0.dp)) {
                Text(
                    text = item.ampm,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()) {
                Text(
                    text = if (item.hour.toString().length == 1) {"0" + item.hour.toString()} else {item.hour.toString()}
                            + " : "
                            + if (item.minute.toString().length == 1) {"0" + item.minute.toString()} else {item.minute.toString()},
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 20.dp)
            ) {
                val myDate = item.alarm_date
                if ("월" in myDate && "화" in myDate && "수" in myDate && "목" in myDate && "금" in myDate && "토" !in myDate && "일" !in myDate) {
                    Text(text = "주중", modifier = Modifier.padding(start = 5.dp))
                }
                else if ("월" !in myDate && "화" !in myDate && "수" !in myDate && "목" !in myDate && "금" !in myDate && "토" in myDate && "일" in myDate) {
                    Text(text = "주말", color= Color.Red, modifier = Modifier.padding(start = 5.dp))
                }
                else if ("월" in myDate && "화" in myDate && "수" in myDate && "목" in myDate && "금" in myDate && "토" in myDate && "일" in myDate) {
                    Text(text = "매일", color= Color.Black, modifier = Modifier.padding(start = 5.dp))
                }
                else {
                    if ("월" in myDate) {
                        Text(text = "월")
                    } else {
                        Text(text = "월", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if ("화" in myDate) {
                        Text(text = " 화")
                    } else {
                        Text(text = " 화", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if ("수" in myDate) {
                        Text(text = " 수")
                    } else {
                        Text(text = " 수", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if ("목" in myDate) {
                        Text(text = " 목")
                    } else {
                        Text(text = " 목", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if ("금" in myDate) {
                        Text(text = " 금")
                    } else {
                        Text(text = " 금", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if ("토" in myDate) {
                        Text(text = " 토")
                    } else {
                        Text(text = " 토", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if ("일" in myDate) {
                        Text(text = " 일")
                    } else {
                        Text(text = " 일", color= Color.Black.copy(alpha = 0.2f))
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScaffold()
}

// 색상 벡터값 받아서 컬러로 바꿔줌
fun String.toColor() = Color(android.graphics.Color.parseColor(this))

// 더미 데이터 생성
data class Alarm (
    val alarm_id: Int,
    val title: String,
    val hour: Int,
    val minute: Int,
    val ampm: String,
    val alarm_date: List<String>
)
val alarm1 = Alarm(
    0,
    "장덕모임",
    8,
    45,
    "오후",
    listOf("월", "화", "수", "목", "금", "토", "일")
)
val alarm2 = Alarm(
    1,
    "미라클 모닝",
    6,
    15,
    "오전",
    listOf("화", "금")
)
val alarm3 = Alarm(
    2,
    "새벽 기도",
    4,
    10,
    "오전",
    listOf("월", "화", "수", "목", "금")
)
val alarm4 = Alarm(
    3,
    "주말 아침운동",
    8,
    5,
    "오전",
    listOf("토", "일")
)
val alarm5 = Alarm(
    4,
    "주말 아침운동",
    8,
    5,
    "오전",
    listOf("토", "일")
)
val alarm6 = Alarm(
    5,
    "주말 아침운동",
    8,
    5,
    "오전",
    listOf("토", "일")
)
val alarm7 = Alarm(
    6,
    "주말 아침운동",
    8,
    5,
    "오전",
    listOf("토", "일")
)
val aaa = listOf(alarm1, alarm2, alarm3, alarm4, alarm5, alarm6, alarm7)

data class Noti(
    val alert_id: Int,
    val profile_img: String,
    val content: String,
    val type: String
)

val noti1 = Noti(
    0,
    "https://cdn-icons-png.flaticon.com/512/149/149071.png",
    "'미라클모닝' 그룹 초대입니다.",
    "INVITE"
)

val noti2 = Noti(
    1,
    "https://ps.w.org/user-avatar-reloaded/assets/icon-256x256.png?rev=2540745",
    "'딘딘'님이 초대를 거절하셨습니다.",
    "BANN"
)
val notiSample = listOf(noti1, noti2)