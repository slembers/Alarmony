package com.slembers.alarmony.feature.ui.common


import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.NavController
import com.slembers.alarmony.feature.common.NavItem


fun showDialog(
    title:String,
    text: String,
    context: Context,
    navController: NavController
    ) {
    val builder = AlertDialog.Builder(context)
    builder
        .setTitle("${title}")
        .setMessage("${text}")
        .setPositiveButton("OK") { dialog, which -> navController.navigate(NavItem.LoginScreen.route) }
//        .setNegativeButton("Cancel") { dialog, which -> }
        .show()
}