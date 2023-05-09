package com.slembers.alarmony.feature.ui.common
import android.content.Context
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun ShowAlertDialog(
    showDialog: Boolean,
    title: String,
    text: String,
    context: Context,
    onDialogDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDialogDismiss,
            title = {
                Text("${title}")
            },
            text = {
                Text("${text}")
            },
            confirmButton = {
                TextButton(
                    onClick = onDialogDismiss
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDialogDismiss
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}