package com.slembers.alarmony.feature.ui.common
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
    onDialogDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDialogDismiss,
            title = {
                Text("Title")
            },
            text = {
                Text("Message")
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