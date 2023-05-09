package com.slembers.alarmony.feature.ui.common

import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext

fun showDialog(context) {
    val builder = AlertDialog.Builder(requireContext(context))
    builder.setMessage("This is an AlertDialog")
        .setPositiveButton("OK") { dialog, which -> }
        .setNegativeButton("Cancel") { dialog, which -> }
        .show()
}