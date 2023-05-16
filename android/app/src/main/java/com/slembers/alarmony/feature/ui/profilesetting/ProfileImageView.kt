package com.slembers.alarmony.feature.ui.profilesetting

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(80.dp)
                .clip(shape = CircleShape)
        )
    } else {
        AsyncImage(
            model = profileImage,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.account_circle),
            modifier = Modifier
                .size(80.dp)
                .clip(shape = CircleShape)
        )
    }
}