package com.slembers.alarmony.feature.groupDetails

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Preview
@Composable
fun GroupDetailsBoardBtn(
    isCheck : Boolean = false,
    modifier : Modifier = Modifier,
    onClick : () -> Unit = {}
) {
    TextButton(
        colors = ButtonDefaults.buttonColors(
            contentColor = AlarmisCheck(isCheck, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.background),
            containerColor = AlarmisCheck(isCheck, MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.primary)
        ),
        onClick = onClick,
        modifier = modifier,
        content = {
            Icon(
                imageVector = AlarmisCheck(isCheck, Icons.Default.Check, Icons.Default.Campaign),
                contentDescription = null
            )
            Text(
                text = AlarmisCheck(isCheck, "일어났어요", "알람보내기"),
                style = TextStyle(
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                ),
                maxLines = 1
            )
        }
    )
}

private fun <T> AlarmisCheck(isCheck : Boolean, True : T, False : T) : T {
    return isCheck.let { check ->
        when(check) {
            true -> True
            false -> False
        }
    }
}