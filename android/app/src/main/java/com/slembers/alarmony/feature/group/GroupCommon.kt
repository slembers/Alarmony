package com.slembers.alarmony.feature.group

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.slembers.alarmony.R

@Composable
@ExperimentalMaterial3Api
fun GroupText(
    title : String
) {
    Text(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(start = 20.dp, top = 0.dp, bottom = 0.dp, end = 0.dp),
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = Color.Black
    )
}

@Composable
@ExperimentalMaterial3Api
fun groupDateElementBox(
    text : String
) {

    var isCheck = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(41.dp)
            .padding(5.dp)
            .clip(CircleShape)
            .wrapContentSize(Alignment.Center),
        content = {
            TextButton(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    isCheck.value = !isCheck.value
                    Log.i("click event","isCheck value : ${isCheck.value}")
                },
                colors = ButtonDefaults.textButtonColors(
                    if (!isCheck.value) {
                        MaterialTheme.colorScheme.primary
                    }
                    else
                        MaterialTheme.colorScheme.background
                ),
                contentPadding = PaddingValues(0.dp),
                content = { Text(text = text) }
            )
        }
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun profileElement(
    image : String?,
    nickname : String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Box( modifier = Modifier
            .size(50.dp)
            .wrapContentSize(Alignment.Center),
            content = {
                if (image == null)
                    Image(
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(55.dp),
                        painter = painterResource(id = R.drawable.baseline_account_circle_24),
                        contentDescription = null
                    )
                else
                    GlideImage(
                        model = image,
                        contentDescription = "Translated description of what the image contains"
                    )
            }
        )
        Text(
            text = nickname
        )
    }
}