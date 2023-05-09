package com.slembers.alarmony

//import com.slembers.alarmony.feature.common.NavController2
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.content.ContextCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
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


    // 액티비티간 데이터를 주고 받기 위함
    @RequiresApi(Build.VERSION_CODES.M)
    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(ContentValues.TAG, "Request permission: " + result.resultCode)
        }

    // 백그라운드 권한 설정
    @RequiresApi(Build.VERSION_CODES.O)
    fun requestAlertPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // check if we already  have permission to draw over other apps
            if (!Settings.canDrawOverlays(this)) {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("백그라운드에서 재생")
                alert.setMessage("앱 기능이 제대로 작동할 수 있도록 앱이 백그라운드에서 실행되도록 해주십시오.")
                alert.setPositiveButton("Ok") { _: DialogInterface?, _: Int ->
                    requestDrawOverlay()
                }
                alert.setNegativeButton("Cancel") { dialog: DialogInterface, _: Int -> finish() }
                alert.show()
            }
            requestBatteryOptimizationPermission()
        }
    }

    // 오버레이 권한 설정
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestDrawOverlay() {
        // if not construct intent to request permission
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:" + applicationContext.packageName)
        )
        resultLauncher.launch(intent)
    }

    // 배터리 최적화 제외 권한 설정
    @RequiresApi(Build.VERSION_CODES.O)
    fun requestBatteryOptimizationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            intent.data = Uri.parse("package:" + this.applicationContext.packageName)
            checkIntentAndStart(this, intent)
        } else {
            val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
            if (checkIntentAndStart(this, intent))
                Toast.makeText(
                    this,
                    "Please enable battery optimizations switch",
                    Toast.LENGTH_LONG
                ).show()
        }
    }

    // 인텐트 확인
    private fun checkIntentAndStart(context: Context, intent: Intent): Boolean {
        intent.resolveActivity(context.packageManager)?.let {
            context.startActivity(intent)
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

}