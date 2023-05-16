package com.slembers.alarmony.feature.screen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.TimeFormat
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import com.slembers.alarmony.feature.alarm.Alarm
import com.slembers.alarmony.feature.alarm.AlarmDto
import com.slembers.alarmony.feature.alarm.saveAlarm
import com.slembers.alarmony.feature.common.CardBox
import com.slembers.alarmony.feature.common.CardDivider
import com.slembers.alarmony.feature.common.CardTitle
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.common.ui.compose.GroupCard
import com.slembers.alarmony.feature.common.ui.compose.GroupTitle
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.ui.common.AnimationRotation
import com.slembers.alarmony.feature.ui.common.CommonDialog
import com.slembers.alarmony.feature.ui.group.GroupBottomButtom
import com.slembers.alarmony.feature.ui.group.GroupContent
import com.slembers.alarmony.feature.ui.group.GroupInvite
import com.slembers.alarmony.feature.ui.group.GroupSound
import com.slembers.alarmony.feature.ui.group.GroupSubjet
import com.slembers.alarmony.feature.ui.group.GroupToolBar
import com.slembers.alarmony.feature.ui.group.GroupTypeButton
import com.slembers.alarmony.feature.ui.group.GroupVolume
import com.slembers.alarmony.model.db.dto.MemberDto
import com.slembers.alarmony.network.service.GroupService
import com.slembers.alarmony.util.DisplayDpUtil
import com.slembers.alarmony.util.Sound
import com.slembers.alarmony.util.WifiUtil
import com.slembers.alarmony.util.showToast
import com.slembers.alarmony.viewModel.GroupViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime

@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class GroupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navController: NavHostController = rememberNavController()
            val viewModel by viewModels<GroupViewModel>()
            viewModel.onChangeSound(Sound())

            NavHost(
                navController = navController,
                startDestination = NavItem.Group.route
            ) {
                composable(route = NavItem.Group.route) {
                    GroupScreen(
                        navController = navController, viewModel = viewModel
                    )
                }
                composable(route = NavItem.GroupInvite.route) {
                    InviteScreen(
                        navController = navController, viewModel = viewModel
                    )
                }
                composable(route = NavItem.Sound.route) {
                    SoundScreen(
                        navController = navController, viewModel = viewModel
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("GroupActivity", "[그룹생성] Activity 시작")
        Log.d("GroupActivity", "GroupActivity 시작")
        val net = WifiUtil.isNetworkConnected(this.application)
        if (net.not()) {
            val intent = Intent(this, MemberActivity::class.java)
            startActivity(intent)
            finish()
            showToast(this, "네트워크 연결을 확인해주세요.")
        }
        Log.d("wifiUtil", "GroupActivity 네트워크 연결상태 확인 : $net")
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("GroupActivity", "[그룹생성] Activity 종료")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Preview
@Composable
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
fun GroupScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: GroupViewModel = viewModel()
) {

    val title by viewModel.title.observeAsState()


   var title2 = remember { mutableStateOf("") }
//    val timePickerState by viewModel.alarmTime.observeAsState()
    val hour by viewModel.hour.observeAsState()
    val minute by viewModel.minute.observeAsState()
    val isWeeks by viewModel.currentWeeks.observeAsState()
    val weeks = listOf("월", "화", "수", "목", "금", "토", "일")
    val members by viewModel.members.observeAsState()
    val soundName by viewModel.sound.observeAsState()
    val content by viewModel.content.observeAsState()
    val vibration by viewModel.vibrate.observeAsState()
    val soundVolume by viewModel.volumn.observeAsState()

    val scrollerState = rememberScrollState()
    val interaction = remember { MutableInteractionSource() }
    val context = LocalContext.current

    var alarmTitle by remember { mutableStateOf(title ?: "") }
    var alarmContent  by remember { mutableStateOf(content ?: "") }

    // 초대된 그룹원 확인
    val checkedMember =
        navController.previousBackStackEntry?.savedStateHandle?.get<Set<MemberDto>>("checkedMember")
    Log.d("checked", "[그룹생성] 선택한 멤버 : ${checkedMember.toString()}")
    val isClosed = remember { mutableStateOf(false) }
    val alertContext = remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }
    // 디스플레이의 너비를 구하는 변수
    val px = context.applicationContext.resources?.displayMetrics?.widthPixels
    val displayWidth = DisplayDpUtil.px2dp(px!!, context)

    Log.d("checked", "[그룹생성] 선택한 크기 : ${px.dp}")
    Log.d("checked", "[그룹생성] 선택한 크기 : $displayWidth")

    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(
                WindowInsets.systemBars.only(
                    WindowInsetsSides.Vertical
                )
            )
            .imePadding()
            .padding(bottom = 20.dp),
        topBar = {
            GroupToolBar(
                title = NavItem.Group.title,
                navClick = { (context as Activity).finish() }
            )
        },
        containerColor = "#F9F9F9".toColor(),
        bottomBar = {
            GroupBottomButtom(
                text = "알람 생성하기",
                enabled = !loading,
                onClick = {

                    val selected = weeks.map {
                        isWeeks?.getValue(it) ?: false
                    }.toList()

                    if (title?.isEmpty() == true) {
                        isClosed.value = true
                        alertContext.value = "제목을 입력해주세요."
                        return@GroupBottomButtom
                    }

                    if (selected.all { !it }) {
                        isClosed.value = true
                        alertContext.value = "요일을 1개이상 선택해주세요."
                        return@GroupBottomButtom
                    }

                    Log.d("viewmodel:ID", "[그룹생성] groupActivity ID : $viewModel")
                    loading = true
                    CoroutineScope(Dispatchers.Main).launch {
                        val groupId = GroupService.addGroupAlarm(
                            title = title,
                            hour = hour ?: 7,
                            minute = minute ?: 0,
                            alarmDate = selected,
                            members = members?.map { it.nickname }?.toList(),
                            soundName = soundName?.soundName,
                            soundVolume = soundVolume,
                            vibrate = vibration
                        )
                        if (groupId != null && groupId > 0) {
                            Log.d("response", "[그룹생성] response : $groupId")
                            suspend fun save() = coroutineScope {
                                async {
                                    saveAlarm(
                                        AlarmDto.toDto(
                                            Alarm(
                                                alarmId = groupId,
                                                title = title!!,
                                                hour = hour!!,
                                                minute = minute!!,
                                                alarmDate = selected,
                                                soundName = soundName?.soundName!!,
                                                soundVolume = soundVolume?.toInt()!!,
                                                vibrate = vibration!!,
                                                host = true,
                                                content = content!!
                                            )
                                        ), context
                                    )
                                }
                            }.await()
                            save()
                            if (groupId > 0) (context as Activity).finish()
                            loading = false
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(10.dp)
                    .verticalScroll(scrollerState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {


                CardBox(
                    title = { CardTitle(title = "알람시간") },
                    content = {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 20.dp,
                                    top = 0.dp,
                                    bottom = 10.dp,
                                    end = 20.dp
                                ),
                        ) {
                            CardDivider()
                            WheelTimePicker(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 45.dp, vertical = 7.dp)
                                    .heightIn(
                                        minOf(140.dp)
                                    ),
                                startTime = LocalTime.of(
                                    LocalTime.now().hour,
                                    LocalTime.now().minute
                                ),
                                minTime = LocalTime.of(0, 0),
                                maxTime = LocalTime.MAX,
                                timeFormat = TimeFormat.AM_PM,
                                size = DpSize(270.dp, 120.dp),
                                rowCount = 3,
                                textStyle = TextStyle(
                                    color = Color.LightGray,
                                    fontSize = 22.sp,
                                    // fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Normal
                                ),
                                textColor = Color(0xFF000000),
                                selectorProperties = WheelPickerDefaults.selectorProperties(
                                    enabled = false,
                                    shape = RoundedCornerShape(18.dp),
                                    color = Color(0xFFFFFFFF).copy(alpha = 0.2f),
                                    border = BorderStroke(4.dp, Color(0xFFf1faee))
                                )
                            ) { snappedTime ->
                                viewModel.updateTimePicker(snappedTime.hour, snappedTime.minute)
                            }
                        } // 알람 시간 CardBox

//                        TimeInput(
//                            state = timePickerState!!,
//                            modifier = Modifier
//                                .padding(
//                                    start = 20.dp,
//                                    top = 10.dp,
//                                    bottom = 0.dp,
//                                    end = 0.dp
//                                )
//                                .focusable(true, interaction)
//                        )
                    }
                )
                CardBox(
                    title = { CardTitle(title = "요일선택") },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 20.dp,
                                    top = 0.dp,
                                    bottom = 10.dp,
                                    end = 20.dp
                                ),
                        ) {
                            //CardDivider()
                            BoxWithConstraints(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val boxSize = this.maxWidth / 8
                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 0.dp,
                                            top = 10.dp,
                                            bottom = 10.dp,
                                            end = 0.dp
                                        ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    // horizontalArrangement = Arrangement.spacedBy(2.dp)
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    items(weeks) { item ->
                                        TextButton(
                                            modifier = Modifier.size(boxSize),
                                            onClick = {
                                                val change = !viewModel.getIsWeek(item)
                                                viewModel.onChangeWeek(item, change)
                                                Log.d(
                                                    "click event",
                                                    "[그룹생성] : $item value : ${
                                                        viewModel.getIsWeek(item)
                                                    }"
                                                )
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                contentColor = Color.Black,
                                                containerColor =
                                                viewModel.getIsWeek(item).let {
                                                    if (it) {
                                                        "#91B4D8".toColor()
                                                    } else {
                                                        MaterialTheme.colorScheme.background
                                                    }
                                                }
                                            ),
                                            content = {
                                                Text(text = item)
                                            }
                                        )
                                    }//items
                                }//LazyRow
                            }
                        }


                    }
                ) //요일 선택 카드 박스


                /**
                 * 알람 설정 Card
                 */
                CardBox(
                    title = { CardTitle(title = "알람설정") },
                    content = {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 20.dp,
                                    top = 0.dp,
                                    bottom = 0.dp,
                                    end = 20.dp
                                ),
                        ) {
                            CardDivider()
                            Row(
                                modifier = Modifier
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(horizontalArrangement = Arrangement.Start) {
                                    Text(
                                        "알람명",
                                        textAlign = TextAlign.Start
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                BasicTextField(value = alarmTitle,
                                    onValueChange = {
                                        alarmTitle = it
                                        Log.d("확인","[생성화면] ${alarmTitle}")
                                        viewModel.onChangeTitle(it)},
                                    Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .padding(start = 22.dp) // margin left and right
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 12.dp), // inner padding
                                        ) {
                                            if (alarmTitle.isEmpty()) {
                                                Text(
                                                    text = "알람명",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = Color.LightGray
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }

                                )
                            }
                            /*  GroupVolume(
                                  volume = soundVolume ?: 7f,
                                  setVolume = { viewModel.onChangeVolume(it) }
                              )*/
                            CardDivider()

                            /** 알람 소개 **/
                            Row(
                                modifier = Modifier
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(horizontalArrangement = Arrangement.Start) {
                                    Text(
                                        "알람소개",
                                        textAlign = TextAlign.Start
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                BasicTextField(value = alarmContent,
                                    onValueChange = {
                                        alarmContent = it
                                        Log.d("확인","[생성화면] ${alarmContent}")
                                        viewModel.onChangeContent(it)},
                                    Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .padding(start = 10.dp) // margin left and right
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 12.dp), // inner padding
                                        ) {
                                            if (alarmContent.isEmpty()) {
                                                Text(
                                                    text = "알람소개",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = Color.LightGray
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }

                                )
                            }
                            CardDivider()



                            GroupVolume(
                                volume = soundVolume ?: 7f,
                                setVolume = { viewModel.onChangeVolume(it) }
                            )
                            CardDivider()
                            GroupSound(
                                navController = navController,
                                sound = soundName?.soundName,
                            )
                            CardDivider()
                            GroupTypeButton(
                                isVibrate = vibration ?: true,
                                viewModel = viewModel
                            )

                        }
                    }
                )
                /**
                 * ???
                 */
    /*            CardBox(
                    title = { CardTitle(title = "???") },
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 20.dp,
                                    top = 0.dp,
                                    bottom = 0.dp,
                                    end = 20.dp
                                ),
                        ) {
                            CardDivider()
                            Row(
                                modifier = Modifier
                                    .padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(horizontalArrangement = Arrangement.Start) {
                                    Text(
                                        "알람명",
                                        textAlign = TextAlign.Start
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                BasicTextField(value = alarmTitle,
                                    onValueChange = {
                                        alarmTitle = it
                                        Log.d("확인","[생성화면] ${alarmTitle}")
                                        viewModel.onChangeTitle(it)},
                                    Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .padding(start = 22.dp) // margin left and right
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 12.dp), // inner padding
                                        ) {
                                            if (alarmTitle.isEmpty()) {
                                                Text(
                                                    text = "알람명",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Normal,
                                                    color = Color.LightGray
                                                )
                                            }
                                            innerTextField()
                                        }
                                    }

                                )
                            }
                            *//*  GroupVolume(
                                  volume = soundVolume ?: 7f,
                                  setVolume = { viewModel.onChangeVolume(it) }
                              )*//*
                            CardDivider()





                            *//*   GroupSound(
                                   navController = navController,
                                   sound = soundName?.soundName,
                               )*//*
                            //CardDivider()

                            *//** 초대하기 **//*

                            *//*   GroupTypeButton(
                                   isVibrate = vibration ?: true,
                                   viewModel = viewModel
                               )*//*
                        }
                    }
                )
*/


                /*
                                GroupCard(
                                    content = {

                                    }
                                )*/

                /** 알람 설명 **/

        /*        GroupCard(
                    content = {
                        OutlinedTextField(
                            value = content!!,
                            onValueChange = { viewModel.onChangeContent(it) },
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Monospace,
                                fontStyle = FontStyle.Normal
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = "#FFFFFF".toColor().copy(alpha = 0.5f),
                                unfocusedBorderColor = "#FFFFFF".toColor()
                                    .copy(alpha = 0.5f)
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
                                    BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.background
                                    ),
                                    MaterialTheme.shapes.extraSmall
                                ),
                            placeholder = {
                                if (content?.isEmpty()!!) {
                                    Text(
                                        text = "그룹설명을 작성해주세요..",
                                        modifier = Modifier.fillMaxWidth(1f),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = Color.LightGray
                                    )
                                }
                            }
                        )
                    }
                )*/


                /**
                 * 그룹초대
                 */
                GroupInvite(
                    navController = navController,
                    members = members ?: mutableListOf()
                )


                /*   GroupSound(
                       navController = navController,
                       sound = soundName?.soundName,
                   )*/


                /*     GroupTypeButton(
                         isVibrate = vibration ?: true,
                         viewModel = viewModel
                     )*/

                /*
                      GroupVolume(
                          volume = soundVolume ?: 7f,
                          setVolume = { viewModel.onChangeVolume(it) }
                      )*/

            }
            if (isClosed.value) {
                CommonDialog(
                    title = "알림",
                    context = alertContext.value,
                    isClosed = isClosed,
                    isButton = false
                )
            }
        }
    )
    if (loading) {
        AnimationRotation()
    }
}

@Composable
fun SimpleTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    placeholderText: String = "",
    fontSize: Dp = 15.dp,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    cursorBrush: Brush = SolidColor(Color.Black),
) {
    BasicTextField(modifier = modifier
        .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        singleLine = singleLine,
        maxLines = maxLines,
        enabled = enabled,
        readOnly = readOnly,
        interactionSource = interactionSource,
        textStyle = textStyle,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        onTextLayout = onTextLayout,
        cursorBrush = cursorBrush,
        decorationBox = { innerTextField ->
            Row(
                modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (leadingIcon != null) leadingIcon()
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(
                        placeholderText,
                        style = textStyle
                    )
                    innerTextField()
                }
                if (trailingIcon != null) trailingIcon()
            }
        }
    )
}