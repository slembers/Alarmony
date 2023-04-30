package com.slembers.alarmony.feature.common.ui.compose

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Checkbox
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.data.MemberData
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.model.db.SoundItem
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
    onClick : () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 0.dp,
                end = 5.dp
            )
            .clickable(
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
    onChangeValue : (String) -> Unit
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
            ),
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
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background),
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
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
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
    nickname : String
) {

    BoxWithConstraints(
        modifier = Modifier
            .width(60.dp)
            .height(70.dp)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(this.maxWidth)
                .padding(0.dp),
            painter = painterResource(id = R.drawable.baseline_account_circle_24),
            contentDescription = null)
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

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupInvite(
    profiles : List<MemberData>
) {

    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    top = 0.dp,
                    bottom = 0.dp,
                    end = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            userScrollEnabled = true

        ) {
            items(profiles) {
                item ->
                    GroupDefalutProfile(item.nickname)
            }
        }
    }
}
@Composable
@ExperimentalMaterial3Api
fun SoundChooseGrid(
    modifier: Modifier = Modifier.width(320.dp),
    itemList: List<SoundItem> = (1..10).map {
        SoundItem(
            painterResource(id = R.drawable.main_app_image_foreground),
            "sound$it") }.toList()
) {
    var checkbox by remember { mutableStateOf(itemList[0].soundName) }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 100.dp),
        content = {
            items(itemList) {
                soundIcon(
                    image = it.soundImage,
                    soundname = it.soundName,
                    onClick = checkbox.equals(it.soundName),
                    checkBox = { checkbox = it }
                )
            }
        })
}

@Composable
fun soundIcon(
    image : Painter? = painterResource(id = R.drawable.main_app_image_foreground),
    soundname : String,
    onClick : Boolean = false,
    checkBox : ((String) -> Unit) =  {}
) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(4.dp)
            .size(100.dp)
            .shadow(
                elevation = 5.dp,
                ambientColor = Color.Black,
                spotColor = Color.Black,
                shape = RoundedCornerShape(20.dp)
            )
            .clip(MaterialTheme.shapes.medium)
            .background(
                if (!onClick)
                    MaterialTheme.colorScheme.background
                else
                    MaterialTheme.colorScheme.primary
            )
            .fillMaxWidth()
            .clickable { checkBox(soundname) },
        content = {
            Image(
                modifier = Modifier
                    .size(
                        width = this.maxWidth,
                        height = this.maxHeight
                    )
                    .padding(5.dp),
                painter = image!!,
                contentDescription = soundname)
        }
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun CurrentInvite() {
    CardBox(
        title = { CardTitle(title = "초대인원") },
        content = {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 5.dp,
                        bottom = 10.dp,
                        end = 0.dp
                    ),
                userScrollEnabled = true
            ) {
                items( count = 8 ) {
                    GroupDefalutProfile(
                        image = painterResource(id = R.drawable.account_circle),
                        nickname = "ssafy01",
                        newMember = true
                    )
                }
            }
        }
    )
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupDefalutProfile(
    image : Painter = painterResource(id = R.drawable.baseline_account_circle_24),
    nickname : String = "nothing",
    newMember : Boolean = true
) {

    BoxWithConstraints(
        modifier = Modifier
            .width(60.dp)
            .height(70.dp)
    ) {
        val maxWidth = this.maxWidth
        Column(
            modifier = Modifier
                .width(maxWidth)
                .fillMaxHeight()
                .height(this.maxHeight)
        ) {
            Box(
                modifier = Modifier
                    .width(maxWidth)
                    .weight(1f)
                    .padding(0.dp),
                content = {
                    Image(
                        modifier = Modifier
                            .matchParentSize()
                            .align(Alignment.Center),
                        painter = image,
                        contentDescription = null)
                    if(newMember) {
                        Box(
                            modifier = Modifier
                                .size(maxWidth / 2)
                                .align(Alignment.BottomEnd)
                                .padding(5.dp)
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .size(maxWidth / 4)
                                    .align(Alignment.Center),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.Black,
                                    containerColor = MaterialTheme.colorScheme.error
                                ),
                                content = { Text(
                                    text = "x",
                                    color = Color.Black
                                )},
                                onClick = { /*TODO*/ }
                            )
                        }
                    }
                }
            )
            Text(
                text = nickname,
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 1,
                textAlign = TextAlign.Center,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun SearchInviteMember() {

    var text by remember{ mutableStateOf("") }

    CardBox(
        title = { CardTitle(title = "검색") },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        top = 0.dp,
                        bottom = 10.dp,
                        end = 20.dp
                    )
            ) {
                BasicTextField(
                    value = text,
                    onValueChange = {text = it},
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                    ),
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xffD9D9D9),
                                    shape = MaterialTheme.shapes.extraLarge
                                )
                                .padding(horizontal = 16.dp), // inner padding
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Favorite icon",
                                tint = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.width(width = 8.dp))
                            if (text.isEmpty()) {
                                Text(
                                    text = "닉네임을 입력새해주세요.",
                                    modifier = Modifier.fillMaxWidth(1f),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.LightGray
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                LazyColumn() {
                    items(count = 10) {
                        SearchMember()
                    }
                }
            }
        }
    )
}

@Composable
fun SearchMember() {

    var isClicked by remember { mutableStateOf(false)  }
    val profile by remember { mutableStateOf(null) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 2.dp, end = 20.dp, top = 3.dp, bottom = 1.dp)
            .fillMaxWidth()
            .clickable { isClicked = !isClicked}
    )
    {
        if(profile != null ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profile)
                    .build(),
                contentDescription = "ImageRequest example",
                modifier = Modifier.size(65.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                contentDescription = "ImageRequest example",
                modifier = Modifier.size(65.dp)
            )
        }
        Text(
            text = "SSAFY에 오신걸 환영합니다.",
            fontSize = 17.sp,
            modifier = Modifier.fillMaxWidth(1f)
        )
        Checkbox(
            checked = isClicked,
            onCheckedChange = { isClicked = it }
        )
    }
}