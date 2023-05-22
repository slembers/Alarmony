package com.slembers.alarmony.feature.ui.groupDetails

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import com.slembers.alarmony.feature.common.ui.theme.toColor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask



@Composable
fun GroupDetailsBoardBtn(
    isCheck: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onAlarm: MutableState<Boolean>,
    remainingSec: MutableState<Int>
) {
    if (!onAlarm.value!! && remainingSec.value==60) {
        LaunchedEffect(!isCheck && !onAlarm.value!!) {
            GlobalScope.launch {
                if (!isCheck && !onAlarm.value!!) {
                    while (remainingSec.value!! > 0) {
                        delay(1000) // 1초 대기
                        remainingSec.value = remainingSec.value!! - 1
                    }
                    onAlarm.value = true
                    remainingSec.value = 60
                }
            }
        }
    }
    TextButton(

        colors = ButtonDefaults.buttonColors(
            contentColor = AlarmisCheck(isCheck, onAlarm.value!!, "#EDEDED".toColor(),"#000000".toColor().copy(alpha = 0.7f), "#EDEDED".toColor().copy(alpha = 0.7f)),
            containerColor = AlarmisCheck(isCheck, onAlarm.value!!, "#5EC96B".toColor(), "#7DC3F2".toColor(), "#808080".toColor())
        ),
        onClick = onClick,
        modifier = Modifier.size(width = 40.dp, height = 32.dp),
        content = {
            AlarmisCheck(isCheck, onAlarm.value!!, Icons.Default.Check, Icons.Default.Campaign, null)?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null
                )
            }
            Text(
                text = AlarmisCheck(isCheck, onAlarm.value!!, "", "", remainingSec.value.toString()),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                ),
                maxLines = 1
            )
        }
    )
}

private fun <T> AlarmisCheck(isCheck : Boolean, onAlarm : Boolean, True : T, False : T, sendAlarm : T) : T {
    return when {
        isCheck -> True
        !isCheck && !onAlarm -> sendAlarm
        else -> False
    }
}