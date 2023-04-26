package com.slembers.alarmony.feature.group

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.ui.compose.GroupCard
import com.slembers.alarmony.feature.common.ui.compose.GroupInvite
import com.slembers.alarmony.feature.common.ui.compose.GroupSubjet
import com.slembers.alarmony.feature.common.ui.compose.GroupTimePicker
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.feature.common.ui.compose.GroupWeeks
import com.slembers.alarmony.model.db.dto.MemberDto

@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class GroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GroupScaffold()
        }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupScaffold() {

    Scaffold(
        topBar = {
            groupToolBar()
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.height(50.dp),
                contentPadding = PaddingValues(0.dp),
                content = {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.shapes.extraSmall
                            ),
                        onClick = { /*TODO*/ },
                        content = {
                            Text(
                                text = "저장",
                                color = Color.Black,
                            )
                        }
                    )
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GroupCard(
                    title = { GroupTitle(title = "그룹제목") },
                    content = { GroupSubjet() }
                )
                GroupCard(
                    title = { GroupTitle(title = "알람시간") },
                    content = { GroupTimePicker() }
                )
                GroupCard(
                    title = { GroupTitle(title = "알람요일") },
                    content = { GroupWeeks() }
                )
                GroupCard(
                    title = { GroupTitle(
                        title = "그룹초대",
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_forward),
                                contentDescription = null,
                                modifier = Modifier.padding(2.dp)
                            )
                        })},
                    content = { GroupInvite(
                        profile = MemberDto("dkfsf","sample"))}
                )
                GroupCard(
                    title = { GroupTitle(
                        title = "알람 선택",
                        content = {
                            Row(
                                modifier = Modifier.height(50.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "노래제목",
                                    style = TextStyle(
                                        color = Color.Gray,
                                        fontSize = 15.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Normal
                                    )
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.arrow_forward),
                                    contentDescription = null,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }
                        }
                    )}
                )
                GroupCard(
                    title = { GroupTitle(
                        title = "타입",
                        content = {
                            Row(
                                modifier = Modifier.height(50.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                            }
                        }
                    )},
                )
                GroupCard(
                    title = { GroupTitle(title = "볼륨") },
                    content = {}
                )
            }
        }
    )
}