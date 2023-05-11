package com.slembers.alarmony.feature.ui.groupDetails

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
import com.slembers.alarmony.feature.common.ui.theme.toColor
import kotlinx.serialization.json.JsonNull.content


@Preview
@Composable
fun GroupDetailsBoardBtn(
    isCheck : Boolean = false,
    modifier : Modifier = Modifier,
    onClick : () -> Unit = {},
) {
    TextButton(
        colors = ButtonDefaults.buttonColors(
            contentColor = AlarmisCheck(isCheck, "#00B4D8".toColor(), MaterialTheme.colorScheme.background),
            containerColor = AlarmisCheck(isCheck, MaterialTheme.colorScheme.background, "#00B4D8".toColor())
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
                    fontSize = 12.sp,
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