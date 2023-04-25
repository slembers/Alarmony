package com.slembers.alarmony.feature.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.slembers.alarmony.R


@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun profile() {
    profileElement(image = null, nickname = "sample01")
}

@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupProfile() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.CenterVertically)
            .padding(5.dp)
            .border(
                BorderStroke(0.dp, MaterialTheme.colorScheme.background),
                MaterialTheme.shapes.medium
            )
            .padding(4.dp)
            .border(1.dp, Color.Black, MaterialTheme.shapes.medium)
            .shadow(
                elevation = 1.dp,
                MaterialTheme.shapes.medium,
                ambientColor = Color.Gray
            )
            .background(
                MaterialTheme.colorScheme.background
            ),
        content = {
            Column{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    GroupText(title = "그룹원 초대")
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_circle_outline),
                        contentDescription = null)
                }
                LazyRow(
                    modifier = Modifier
                        .padding(start = 20.dp, top = 5.dp, bottom = 0.dp)
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = true
                ) {
                    items(
                        count = 10
                    ) {
                        profileElement(image = null, nickname = "sample01")
                    }
                }
            }
        }
    )
}