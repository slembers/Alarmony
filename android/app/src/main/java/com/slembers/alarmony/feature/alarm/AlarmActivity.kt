package com.slembers.alarmony.feature.alarm

import android.app.Activity
import android.app.KeyguardManager
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.slembers.alarmony.R
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.feature.alarm.AlarmApi.recordAlarmApi
import com.slembers.alarmony.feature.alarm.AlarmNoti.cancelNotification
import com.slembers.alarmony.feature.alarm.AlarmNoti.runNotification
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.util.DisplayDpUtil
import com.slembers.alarmony.feature.screen.MemberActivity
import com.slembers.alarmony.network.repository.MemberService
import com.slembers.alarmony.util.PresharedUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Timer
import java.util.TimerTask

lateinit var timer: Timer
class AlarmActivity : ComponentActivity() {
    lateinit var repository: AlarmRepository
    lateinit var wakeLock: PowerManager.WakeLock
    lateinit var alarmDto: AlarmDto
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Log.d("myResponse_AlarmActive", "알람 액티비티 활성화")
        MainActivity.prefs = PresharedUtil(application)

        val alarmId = intent.getLongExtra("alarmId", -1L)
        Log.d("ForeA1", alarmId.toString())
        val alarmDao = AlarmDatabase.getInstance(this).alarmDao()
        CoroutineScope(Dispatchers.IO).launch {
            repository = AlarmRepository(alarmDao)
            val alarm = repository.findAlarm(alarmId)
            alarmDto = AlarmDto.toDto(alarm!!)
            runNotification(application, alarmDto!!)
        }
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                    acquire()
                }
            }

        // 3분 뒤 자동으로 꺼짐
        CoroutineScope(Dispatchers.IO).launch {
            timer = Timer()
            var remainingSec = 180 // 3분
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    remainingSec -= 1
                    if (remainingSec == 0) {
                        timer.cancel()
                        setSnoozeAlarm(this@AlarmActivity, alarmDto, 5) // 5분뒤 스누즈
                        cancelNotification()
                        goMain(this@AlarmActivity)
                        this@AlarmActivity.finish()
                    }
                }
            }, 1000, 1000) // 1초 뒤 1초에 한번씩 실행
        }


        // 강제로 화면 키기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
        } else {
            this.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                (WindowManager.LayoutParams.FLAG_FULLSCREEN or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
            )
        }
        val keyguardMgr = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyguardMgr.requestDismissKeyguard(this, null)
        }

        setContent {
            AlarmScreen(alarmDto!!)

            val access = MainActivity.prefs.getString("accessToken","")
            val refresh = MainActivity.prefs.getString("refreshToken","")
            val username = MainActivity.prefs.getString("username","")
            if( access.isNotBlank() || access.isNotEmpty() ) {
                Log.d("application start","토큰을 재발급 받으려고 합니다..")
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        MemberService.reissueToken(username = username, refreshToken = refresh)
                    }
                }
            } else {
                Log.d("application start","로그인을 시도해야 합니다.")
                val intent = Intent(this, MemberActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed() 뒤로 가기 막기
    }
    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        wakeLock.release()
    }
}
//파일의 문자를 식별자로 가져오기 위한 함수
fun getResIdByName(resName: String, resType: String, context: Context): Int {
    return context.resources.getIdentifier(resName, resType, context.packageName)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(alarmDto : AlarmDto) {
    val context = LocalContext.current as Activity
    val isClicked5 = remember { mutableStateOf(false)  }
    val isClicked10 = remember { mutableStateOf(false)  }
    val alarmStartTime = calAlarm(alarmDto)
//    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
//            .verticalScroll(scrollState)
        ,
        containerColor = Color.White,
        content = { innerPadding ->
//            BoxWithConstraints(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.backsun),
//                    contentDescription = null,
//                    contentScale = ContentScale.FillBounds,
//                    modifier = Modifier.fillMaxSize()
//                )
//                // 여기에 다른 콘텐츠 추가
//            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
//                        개인
//
//                    .background(color = "#ecddd6".toColor())
//                        단체
                    .background(color = "#cfffe5".toColor())

                    .padding(innerPadding),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DrawCircle(alarmDto)
                Spacer(modifier = Modifier.height(100.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 20.dp)
                ) {
                    Button(
                        onClick = { isClicked5.value = true },
                        shape = CircleShape,
//                        border = BorderStroke(2.dp, "#c4b3dc".toColor().copy(alpha= 0.1f)),
                        modifier = Modifier
                            .padding(5.dp)
                            .size(90.dp),

                        colors = ButtonDefaults.buttonColors("#f5f5dc".toColor())
//                        colors = ButtonDefaults.buttonColors("#a6d6c5".toColor())
                    ) {
                        Text(
                            text = "5분",
//                            color = Color.White,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize =20.sp
                        )
                    }

                    Button(
                        onClick = {
                            val dateTime = LocalDateTime.now()
                            val formatter = DateTimeFormatter.ISO_DATE_TIME
                            val formattedDateTime = dateTime.format(formatter)
                            recordAlarmApi(formattedDateTime, alarmDto.alarmId) // 알람 정지 시 기록 api
                            cancelNotification()
                            timer.cancel()
                            context.finish()
                            goMain(context)
                        },
                        shape = CircleShape,
//                        border = BorderStroke(4.dp, "#c4b3dc".toColor().copy(alpha= 0.1f)),
                        modifier = Modifier
                            .padding(5.dp)
                            .size(130.dp),
//                        개인
//                        colors = ButtonDefaults.buttonColors("#dfbacf".toColor())
//                    단체
                        colors = ButtonDefaults.buttonColors("#f5f5dc".toColor())

                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Stop,
                            contentDescription = "Setting",
//                           개인
//                            tint = "#EF2828".toColor(),
//                            단체
//                            tint = "#d2a4e5".toColor(),
                            tint = Color.Red,

                            modifier = Modifier.size(80.dp)
                        )
                    }
                    Button(
                        onClick = { isClicked10.value = true },
                        shape = CircleShape,
//                        border = BorderStroke(2.dp, "#c4b3dc".toColor().copy(alpha= 0.1f)),
                        modifier = Modifier
                            .padding(5.dp)
                            .size(90.dp),
//                        개인
//                        colors = ButtonDefaults.buttonColors("#dfbacf".toColor())
//                    단체
                        colors = ButtonDefaults.buttonColors("#f5f5dc".toColor())

                    ) {
                        Text(
                            text = "10분",
//                            color = Color.White,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize =20.sp
                        )
                    }
                }
            }
            if (isClicked5.value) {
                SnoozeNoti(5, isClicked5, context, alarmDto, timer)
            }
            if (isClicked10.value) {
                SnoozeNoti(10, isClicked10, context, alarmDto, timer)
            }
        }
    )
}
@Preview
@Composable
fun DrawCircle(alarmDto : AlarmDto =
                   AlarmDto(1,"",1,1,
    listOf(false,false,false,false,false,false,false),"Normal",1,true,false,"히히")
) {

    val hour = if (alarmDto.hour.toString().length == 1) { "0" + alarmDto.hour.toString() }
    else {
        alarmDto.hour.toString()
    }
    val minute = if (alarmDto.minute.toString().length == 1) { "0" + alarmDto.minute.toString() }
    else {
        alarmDto.minute.toString()
    }


//    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.pulse))
////    val progress by animateLottieCompositionAsState(composition)
//    val progress by animateLottieCompositionAsState(
//        composition = composition,
//        iterations = LottieConstants.IterateForever
//    )
//
//
//    LottieAnimation(
//        composition = composition,
//        progress = { progress },
//    )

    val context = LocalContext.current
    val px = LocalContext.current.resources.displayMetrics.widthPixels
    val width = DisplayDpUtil.px2dp(px,context)
//    val width = 2.dp
    val outerRadius = width

    val infiniteTransition = rememberInfiniteTransition()
    val innerCircle by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    val outterCircle by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )
    val outterSize by infiniteTransition.animateFloat(
        initialValue = outerRadius,
        targetValue = outerRadius + 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )


    val innerSize by infiniteTransition.animateFloat(
        initialValue = outerRadius,
        targetValue = outerRadius - 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    val motionColor by infiniteTransition.animateColor(
//        개인 보라색
//        initialValue = "#c095e4".toColor(),
//      단체 파랑
        initialValue = "#5eadf3".toColor(),

//        targetValue = Color.White,
//      개인 회색
//
        targetValue ="#d2d5e4".toColor(),
        animationSpec = infiniteRepeatable(
//            animation = spring(),
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    val motionColor2 by infiniteTransition.animateColor(
//        개인회색
        initialValue = "#d2d5e4".toColor(),

//        targetValue = Color.White,
//      흰색
        targetValue = Color.White,
        animationSpec = infiniteRepeatable(
//            animation = spring(),
            animation = tween(durationMillis = 1500),
            repeatMode = RepeatMode.Reverse
        )
    )

    Canvas(
        modifier = Modifier.size(300.dp)
    ) {
        val outerRadius = size.width / 2
        drawCircle(
            color = motionColor.copy(alpha = innerCircle - 0.15f),
            radius = innerSize,
            center = center,
            style = Stroke(width = 20.dp.toPx())
        )

        drawCircleWithInnerCircle(center, outterSize / 0.8f, motionColor2.copy(alpha = outterCircle))
        drawCircleWithInnerCircle(center, innerSize / 1.04f, motionColor.copy(alpha = innerCircle))
//        개인
//        drawCircleWithInnerCircle(center, innerSize / 1.2f, Color.White.copy(alpha = 0.95f))
//         단체 베이지
        drawCircleWithInnerCircle(center, innerSize / 1.2f, "#cfffe5".toColor().copy(alpha = 0.95f))

        drawIntoCanvas { canvas ->
            val text = "${hour}:${minute}"
            val typeface = Typeface.create("font/roboto_bold.ttf", Typeface.BOLD)
            val paint1 = Paint().asFrameworkPaint().apply {
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 75.sp.toPx()
                color = Color.Black.toArgb()
                setTypeface(typeface)
                isAntiAlias = true
            }
            val calendar: Calendar = Calendar.getInstance()
            var todayDayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)
            val todayDay = when(todayDayOfWeek) {
                1 -> "일"
                2 -> "월"
                3 -> "화"
                4 -> "수"
                5 -> "목"
                6 -> "금"
                7 -> "토"
                else -> ""
            }
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("MM/dd")
            val formatted = current.format(formatter)
            val text2 = "${formatted} (${todayDay})"
            val paint2 = Paint().asFrameworkPaint().apply {
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 30.sp.toPx()
                color = Color.Black.toArgb()
                setTypeface(typeface)
                isAntiAlias = true
            }
            val text3 = "${alarmDto.title}"
            val paint3 = Paint().asFrameworkPaint().apply {
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 26.sp.toPx()
                color = Color.Black.toArgb()
                setTypeface(typeface)
                isAntiAlias = true
            }
            val y1 = center.y - ((paint1.descent() + paint1.ascent()) / 2)
            canvas.nativeCanvas.drawText(text, center.x, y1, paint1)
            canvas.nativeCanvas.drawText(text2, center.x, center.y - 150, paint2)
            canvas.nativeCanvas.drawText(text3, center.x, center.y + 250, paint3)
        }
    }

//    val compositions = (1..9).map {
//        rememberLottieComposition(LottieCompositionSpec.RawRes(getResIdByName("ani_$it", "raw", context))).value
//    }
//    val progresses = compositions.map {
//        animateLottieCompositionAsState(
//            composition = it,
//            iterations = LottieConstants.IterateForever
//        )
////    }
//
//    for (i in compositions.indices) {
//        LottieAnimation(
//            composition = compositions[i],
//            progress = { progresses[i].value },
//        )
//    }


}

fun DrawScope.drawCircleWithInnerCircle(center: Offset, innerRadius: Float, innerColor: Color) {

    drawCircle(innerColor, radius = innerRadius - 4.dp.toPx(), center)
}

@Preview
@Composable
fun DefaultView() {
    val alarmDto = AlarmDto(
        0L,
        "장덕모임",
        8,
        45,
        listOf(true, true, true, false, false, true, true),
        "자장가",
        15,
        vibrate = true,
        host = false,
        content = "장덕팸 미라클 모임 파티입니다."
    )
    AlarmScreen(alarmDto = alarmDto)
}