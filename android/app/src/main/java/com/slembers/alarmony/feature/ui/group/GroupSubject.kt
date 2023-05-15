package com.slembers.alarmony.feature.ui.group

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.feature.common.ui.compose.GroupCard

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupSubjet(
    title : String,
    onChangeValue : (String) -> Unit,
    interactionSource: MutableInteractionSource
) {

    var focuse by remember { mutableStateOf(false) }

    GroupCard(
        content = {
            OutlinedTextField(
            value = title,
            onValueChange = onChangeValue,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontStyle = FontStyle.Normal
            ),
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    top = 0.dp,
                    bottom = 0.dp,
                    end = 10.dp
                )
                .fillMaxWidth()
                .focusable(focuse)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.background),
                    MaterialTheme.shapes.extraSmall
                )
                .focusable(enabled = true, interactionSource = interactionSource),
            singleLine = true
            ,
            placeholder = {
                if(!focuse) {
                    Text(
                        text = "그룹제목을 입력해주세요.",
                        modifier = Modifier.fillMaxWidth(1f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.LightGray
                    )
                }
            }
        )}
    )

//    Divider(
//        color = Color.Black,
//        thickness = 1.dp,
//        modifier = Modifier
//            .padding(
//                start = 20.dp,
//                top = 0.dp,
//                bottom = 10.dp,
//                end = 10.dp
//            )
//            .fillMaxWidth()
//    )
}
