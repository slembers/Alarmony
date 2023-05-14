package com.slembers.alarmony.feature.ui.profilesetting

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.slembers.alarmony.R

@Composable
fun ProfileImageView (
    profileImage : String
){
    if (profileImage.isEmpty() || profileImage == "null") {
        Image(
            painter = painterResource(R.drawable.mascot_foreground),
            contentDescription = "프로필 이미지",
            modifier = Modifier
                .size(100.dp)
                .clickable(onClick = {
                    Log.d("profile", "기본 이미지")
                })
        )
        Log.d("profile", "기본 이미지 출력")
    } else {
        AsyncImage(
            model = profileImage,
            contentDescription = "업로드 된 이미지",
            modifier = Modifier
                .size(100.dp)
                .clickable(onClick = {
                    Log.d("profile", "업로드된 이미지")
                })
        )
        Log.d("profile", "업로드된 이미지 출력")
    }
}