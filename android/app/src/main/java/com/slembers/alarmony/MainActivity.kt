package com.slembers.alarmony

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.slembers.alarmony.feature.common.NavController
import com.slembers.alarmony.util.PresharedUtil

@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var prefs : PresharedUtil
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      SharedPreferences 클래스는 앱에 있는 다른 Class보다 먼저 생성되어야함
        prefs = PresharedUtil(application)

        setContent {
            NavController()
            requestAlertPermission() // 권한 실행
        }
    }

    // 백그라운드 권한 설정
    @RequiresApi(Build.VERSION_CODES.O)
    fun requestAlertPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("백그라운드에서 재생")
                alert.setMessage("앱 기능이 제대로 작동할 수 있도록 앱이 백그라운드에서 실행되도록 해주십시오.")
                alert.setPositiveButton("Ok") { _: DialogInterface?, _: Int ->
                    requestDrawOverlay()
                    requestBatteryOptimizationPermission()
                }
                alert.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int -> finish() }
                alert.show()
            }
            requestNotificationPermission()
        }
    }
    // 오버레이 권한 설정
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestDrawOverlay() {
            TedPermission.create()
                .setPermissionListener(object : PermissionListener {
                    override fun onPermissionGranted() {}
                    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {}
                })
                .setPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .check()
    }
    // 배터리 최적화 무시 권한
    fun requestBatteryOptimizationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val packageName = packageName
            val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
    }
    // 알림 권한 요청 (13버전 이상)
    private fun requestNotificationPermission() {
        TedPermission.create()
            .setPermissionListener(object: PermissionListener {
                override fun onPermissionGranted() {}
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {}
            })
            .setPermissions(Manifest.permission.POST_NOTIFICATIONS)
            .check()
    }
}