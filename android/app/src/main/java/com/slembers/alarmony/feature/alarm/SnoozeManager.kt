package com.slembers.alarmony.feature.alarm

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.slembers.alarmony.feature.alarm.AlarmApi.snoozeMessageApi
import com.slembers.alarmony.feature.alarm.AlarmNoti.cancelNotification
import com.slembers.alarmony.feature.common.ui.theme.toColor

@Composable
fun SnoozeNoti(snoozeType : Int, isClicked : MutableState<Boolean>, context : Activity, alarmDto : AlarmDto) {
    val openDialog = remember { mutableStateOf(true)  }
    val newContext = context as Context
    var text = remember { mutableStateOf("") }
    if (openDialog.value) {
        AlertDialog(
            shape = RoundedCornerShape(20.dp),
            onDismissRequest = {
                openDialog.value = false
                isClicked.value = false
            },
            title = {
                Column() {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "스누즈 메세지",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        value = text.value,
                        shape = RoundedCornerShape(10.dp),
                        onValueChange = { text.value = it },
                        textStyle = TextStyle(fontSize = 30.sp, textDecoration = TextDecoration.None),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Gray.copy(0.1f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                }
            },
            buttons = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        modifier = Modifier.width(110.dp).padding(10.dp),

                        onClick = {
                            openDialog.value = false
                            cancelNotification()
                            snoozeMessageApi(text.value, alarmDto.alarmId)
                            Log.d("myResponse", text.value)
                            setSnoozeAlarm(newContext, alarmDto, snoozeType)
                            goMain(context)
                            context.finish()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = "#31AF91".toColor()),
                    ) {
                        Text(text = "확인", fontSize = 20.sp)
                    }
                }
            }
        )
    }
}


fun setSnoozeAlarm(context: Context, alarmDto: AlarmDto, snoozeType: Int) {
    val newTime =
        if (snoozeType == 5) {
            System.currentTimeMillis() + (5 * 60 * 1000) // 스누즈 5분
        } else {
            System.currentTimeMillis() + (10 * 60 * 1000) // 스누즈 10분
        }
    val intent = Intent(context, AlarmReceiver::class.java)
    intent.putExtra("alarmId", alarmDto.alarmId)
    intent.putExtra("isSnooze", true)
    val myPendingIntent : Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_MUTABLE
    } else {
        PendingIntent.FLAG_MUTABLE
    }
    val alarmIntentRTC: PendingIntent =
        PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            myPendingIntent
        )
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmInfo = AlarmManager.AlarmClockInfo(newTime, alarmIntentRTC)
    alarmManager.setAlarmClock(alarmInfo, alarmIntentRTC)

    val receiver = ComponentName(context, AlarmReceiver::class.java)
    context.packageManager.setComponentEnabledSetting(
        receiver,
        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
        PackageManager.DONT_KILL_APP
    )
    Log.d("myResponse", "${snoozeType}분 뒤 울리는 스누즈 알람 세팅완료")
}