package com.slembers.alarmony.feature.sendAlarm

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.feature.alarm.AlarmDatabase
import com.slembers.alarmony.feature.alarm.AlarmDto
import com.slembers.alarmony.feature.alarm.AlarmRepository
import com.slembers.alarmony.feature.alarm.drawCircleWithInnerCircle
import com.slembers.alarmony.feature.alarm.goMain
import com.slembers.alarmony.feature.common.ui.theme.toColor
import com.slembers.alarmony.feature.sendAlarm.SendAlarmNoti.cancelSendAlarmNotification
import com.slembers.alarmony.feature.sendAlarm.SendAlarmNoti.runSendAlarmNotification
import com.slembers.alarmony.util.DisplayDpUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SendAlarmActivity : ComponentActivity() {
    lateinit var repository: AlarmRepository
    lateinit var wakeLock: PowerManager.WakeLock
    lateinit var alarmDto: AlarmDto
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val alarmId = intent.getLongExtra("alarmId", -1L)
        val alarmDao = AlarmDatabase.getInstance(this).alarmDao()
        CoroutineScope(Dispatchers.IO).launch {
            repository = AlarmRepository(alarmDao)
            val alarm = repository.findAlarm(alarmId)
            alarmDto = AlarmDto.toDto(alarm!!)
            runSendAlarmNotification(application, alarmDto!!)
        }
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::MyWakelockTag").apply {
                    acquire()
                }
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
            SendAlarmScreen(alarmDto!!)
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed() 뒤로 가기 막기
    }
    override fun onDestroy() {
        wakeLock.release()
        super.onDestroy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendAlarmScreen(alarmDto : AlarmDto) {

    val context = LocalContext.current as Activity
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
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
                        onClick = {
                            cancelSendAlarmNotification()
                            context.finish()
                            goMain(context)
                        },
                        shape = CircleShape,
//                        border = BorderStroke(2.dp, "#63B1C2".toColor()),
                        modifier = Modifier
                            .padding(5.dp)
                            .size(130.dp),
                        colors = ButtonDefaults.buttonColors("#7ADBEF".toColor())
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Stop,
                            contentDescription = "Setting",
                            tint = "#ff8f82".toColor(),
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DrawCircle(alarmDto : AlarmDto) {
    val infiniteTransition = rememberInfiniteTransition()
    val innerCircle by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    val context = LocalContext.current
    val px = LocalContext.current.resources.displayMetrics.widthPixels
    val width = DisplayDpUtil.px2dp(px,context)
    val outerRadius = width

    val innerSize by infiniteTransition.animateFloat(
        initialValue = outerRadius,
        targetValue = outerRadius - 40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
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

    val motionColor by infiniteTransition.animateColor(
        initialValue = "#0f5e9c".toColor(),
        targetValue ="#2389da".toColor(),
        animationSpec = infiniteRepeatable(
//            animation = spring(),
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val motionColor2 by infiniteTransition.animateColor(
        initialValue = "#1ca3ec".toColor(),
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
            color = Color.White.copy(alpha = innerCircle - 0.15f),
            radius = innerSize,
            center = center,
            style = Stroke(width = 20.dp.toPx())
        )

        drawCircleWithInnerCircle(center, outterSize / 0.8f, motionColor2.copy(alpha = outterCircle))
        drawCircleWithInnerCircle(center, innerSize / 1.04f, motionColor.copy(alpha = innerCircle))
        drawCircleWithInnerCircle(center, innerSize / 1.2f, Color.White.copy(alpha = 0.9f))

        drawIntoCanvas { canvas ->
            val text = "알람 배달"
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
        content = "장덕동 사는 사람 모여라"
    )
    SendAlarmScreen(alarmDto = alarmDto)
}