package com.slembers.alarmony.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission

fun hasWriteExternalStoragePermission(context: Context): Boolean {
    val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val result = ContextCompat.checkSelfPermission(context, permission)
    return result == PackageManager.PERMISSION_GRANTED
}

fun requestWriteExternalStoragePermission() {
    TedPermission.create()
        .setPermissionListener(object: PermissionListener {
            override fun onPermissionGranted() {}
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {}
        })
        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .check()
}