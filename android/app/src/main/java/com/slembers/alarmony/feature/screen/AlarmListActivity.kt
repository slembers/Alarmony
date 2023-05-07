package com.slembers.alarmony.feature.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.alarm.Alarm
import com.slembers.alarmony.feature.alarm.aaa
import com.slembers.alarmony.feature.alarm.notiSample
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.theme.notosanskr
import com.slembers.alarmony.feature.common.ui.theme.toColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmListScreen(navController : NavHostController) {
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
                        IconButton(onClick = { navController.navigate(NavItem.NotiListScreen.route) }) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notification",
                                tint = Color.Black,
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    } else {
                        IconButton(onClick = { navController.navigate(NavItem.NotiListScreen.route) }) {
                            Icon(
                                imageVector = Icons.Outlined.NotificationsActive,
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
                onClick = { navController.navigate(NavItem.Group.route) },
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
            val aTime : Int
            val ampm : String
            if (item.hour >= 0 && item.hour < 12) {
                ampm = "오전"
                if (item.hour == 0) {
                    aTime = 12
                } else {
                    aTime = item.hour
                }
            } else {
                ampm = "오후"
                if (item.hour > 12) {
                    aTime = item.hour - 12
                } else {
                    aTime = item.hour
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 25.dp, top = 5.dp, bottom = 0.dp)) {
                Text(
                    text = ampm,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .fillMaxWidth()) {
                Text(
                    text = if (aTime.toString().length == 1) {"0" + aTime.toString()} else {aTime.toString()}
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
                if (myDate[0] == true && myDate[1] == true && myDate[2] == true && myDate[3] == true && myDate[4] == true && myDate[5] == false && myDate[6] == false) {
                    Text(text = "주중", modifier = Modifier.padding(start = 5.dp))
                }
                else if (myDate[0] == false && myDate[1] == false && myDate[2] == false && myDate[3] == false && myDate[4] == false && myDate[5] == true && myDate[6] == true) {
                    Text(text = "주말", color= Color.Red, modifier = Modifier.padding(start = 5.dp))
                }
                else if (myDate[0] == true && myDate[1] == true && myDate[2] == true && myDate[3] == true && myDate[4] == true && myDate[5] == true && myDate[6] == true) {
                    Text(text = "매일", color= Color.Black, modifier = Modifier.padding(start = 5.dp))
                }
                else {
                    if (myDate[0] == true) {
                        Text(text = "월")
                    } else {
                        Text(text = "월", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[1] == true) {
                        Text(text = " 화")
                    } else {
                        Text(text = " 화", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[2] == true) {
                        Text(text = " 수")
                    } else {
                        Text(text = " 수", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[3] == true) {
                        Text(text = " 목")
                    } else {
                        Text(text = " 목", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[4] == true) {
                        Text(text = " 금")
                    } else {
                        Text(text = " 금", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[5] == true) {
                        Text(text = " 토")
                    } else {
                        Text(text = " 토", color= Color.Black.copy(alpha = 0.2f))
                    }
                    if (myDate[6] == true) {
                        Text(text = " 일")
                    } else {
                        Text(text = " 일", color= Color.Black.copy(alpha = 0.2f))
                    }

                }
            }
        }
    }
}