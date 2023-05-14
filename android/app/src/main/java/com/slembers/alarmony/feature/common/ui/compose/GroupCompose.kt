package com.slembers.alarmony.feature.common.ui.compose

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.model.db.Member
import com.slembers.alarmony.viewModel.GroupSearchViewModel
import kotlinx.serialization.json.JsonNull.content
import java.util.Locale

@Composable
@ExperimentalMaterial3Api
fun GroupText(
    title : String = "폰트",
    fontsize : TextUnit = 20.sp
) {
    Text(
        text = title,
        style = TextStyle(
            color = Color.Black,
            fontSize = fontsize,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        modifier = Modifier.padding(
            start = 20.dp,
            top = 0.dp,
            bottom = 0.dp,
            end = 0.dp),
        textAlign = TextAlign.Start
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupTitle(
    title : String,
    modifier: Modifier = Modifier,
    content : @Composable() () -> Unit = {},
    enable : Boolean = false,
    onClick : () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(
                min(50.dp,50.dp)
            )
            .padding(
                start = 0.dp,
                end = 5.dp
            )
            .clickable(
                enabled = enable,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Normal
            ),
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    top = 0.dp,
                    bottom = 0.dp,
                    end = 0.dp
                )
                .weight(1f),
            textAlign = TextAlign.Start
        )
        content()
    }
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupSubjet(
    title : String,
    onChangeValue : (String) -> Unit,
    interactionSource: MutableInteractionSource
) {

    OutlinedTextField(
        value = title!!,
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
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.background),
                MaterialTheme.shapes.extraSmall
            )
            .focusable(enabled = true, interactionSource = interactionSource),
        singleLine = true
    )
    Divider(
        color = Color.Black,
        thickness = 1.dp,
        modifier = Modifier
            .padding(
                start = 20.dp,
                top = 0.dp,
                bottom = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth()
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupCard(
    title : @Composable() () -> Unit,
    content: @Composable() () -> Unit = {}
) {

    Card(
        colors = CardDefaults.cardColors(containerColor = "#FFFFFF".toColor()),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .border(
                BorderStroke(0.dp, MaterialTheme.colorScheme.background),
                MaterialTheme.shapes.medium
            )
            .padding(4.dp)
//            .border(1.dp, Color.Black, MaterialTheme.shapes.medium)
            .shadow(
                elevation = 5.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                shape = RoundedCornerShape(20.dp)
            ),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp),
                content = {
                    title()
                    content()
                }
            )
        }
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupTimePicker(
    state: TimePickerState
) {

    val state = rememberTimePickerState()
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current

    TimeInput(
        state = state,
        modifier = Modifier
            .padding(
                start = 20.dp,
                top = 10.dp,
                bottom = 0.dp,
                end = 0.dp)
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupWeeks() {

    var isCheck = remember { mutableMapOf<String,Boolean>() }
    val width = remember { mutableStateOf(0.dp) }
    var isWeeks = remember{ mutableStateMapOf(
        "월" to true,
        "화" to true,
        "수" to true,
        "목" to true,
        "금" to true,
        "토" to true,
        "일" to true
    )}
    val weeks = listOf<String>("월","화","수","목","금","토","일")

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 5.dp,
                bottom = 5.dp,
                end = 10.dp
            )
    ) {
        var buttonSize = this.maxWidth / 8
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)

        ) {
            items(weeks) {
                TextButton(
                    modifier = Modifier.size(buttonSize),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor =
                        if (isWeeks.getValue(it)) {
                            MaterialTheme.colorScheme.primary
                        }
                        else
                            MaterialTheme.colorScheme.background
                    ),
                    onClick = {
                        isWeeks[it] = !isWeeks.getValue(it)
                        Log.d("click event","isCheck value : ${isWeeks[it]}")
                    },
                    content = {
                        Text(text = it)
                    }
                )
            }
        }
    }
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDefalutProfile(
    nickname : String,
    profileImg : String?
) {

    BoxWithConstraints(
        modifier = Modifier
            .width(60.dp)
            .height(70.dp)
    ) {
        if(profileImg == null) {
            Image(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(this.maxWidth)
                    .clip(CircleShape)
                    .padding(0.dp),
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                contentDescription = null
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profileImg)
                    .build(),
                contentDescription = "ImageRequest example",
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .clip(CircleShape)
                    .size(this.maxWidth)
                    .padding(0.dp)
            )
        }
        Text(
            text = nickname,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            maxLines = 1,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )
    }
}
