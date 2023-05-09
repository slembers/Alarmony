package com.slembers.alarmony.feature.ui.common


import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController

fun showDialog(
    title:String,
    text: String,
    context: Context,
    ) {
    val builder = AlertDialog.Builder(context)
    builder
        .setTitle("${title}")
        .setMessage("${text}")
        .setPositiveButton("OK") { dialog, which -> }
//        .setNegativeButton("Cancel") { dialog, which -> }
        .show()
}