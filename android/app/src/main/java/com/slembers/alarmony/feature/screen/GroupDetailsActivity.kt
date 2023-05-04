package com.slembers.alarmony.feature.screen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.feature.ui.group.GroupToolBar

@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class GroupDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GroupDetailsScreen()
        }
    }
}

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDetailsScreen() {

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            GroupToolBar(
                title = NavItem.Group.title,
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(10.dp)
                    .verticalScroll(
                        state = scrollState,
                        enabled = true),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                GroupDetailsTitle(title = "장덕모임")
                GroupDetailsBoard()
                CardBox(
                    title = { GroupTitle(
                        title = "그룹원 통계",
                        content = { Icon(
                            modifier = Modifier.size(50.dp),
                            imageVector = Icons.Filled.BarChart,
                            contentDescription = null
                        )}
                    )}
                )
                CardBox( title = { GroupTitle(
                    title = "그룹 나가기",
                    content = { Icon(
                        modifier = Modifier.size(50.dp),
                        imageVector = Icons.Filled.ExitToApp,
                        contentDescription = null,
                        tint = Color.Red
                    )}
                )}
                )
            }
        }
    )
}