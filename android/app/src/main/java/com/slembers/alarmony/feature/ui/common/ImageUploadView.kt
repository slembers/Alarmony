package com.slembers.alarmony.feature.ui.common

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ImageUploader(onImageUploadComplete: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {onImageUploadComplete(uri)}
        }
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "이미지 변경",
            Modifier
                .padding(6.dp)
                .clickable { launcher.launch("image/*") },
            style = MaterialTheme.typography.body1.copy(color = Color.Blue)
        )
    }

}
