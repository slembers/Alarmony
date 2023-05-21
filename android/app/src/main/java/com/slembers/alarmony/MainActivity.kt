package com.slembers.alarmony

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.slembers.alarmony.feature.common.NavController
import com.slembers.alarmony.feature.screen.MemberActivity
import com.slembers.alarmony.network.repository.MemberService.reissueToken
import com.slembers.alarmony.util.PresharedUtil
import com.slembers.alarmony.util.WifiUtil
import com.slembers.alarmony.util.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var prefs : PresharedUtil
        var viewModelStore: ViewModelStore =  ViewModelStore()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      SharedPreferences 클래스는 앱에 있는 다른 Class보다 먼저 생성되어야함
        prefs = PresharedUtil(application)
        requestAlertPermission() // 권한 실행
        setContent {
            val access = prefs.getString("accessToken","")
            val refresh = prefs.getString("refreshToken","")
            val username = prefs.getString("username","")
            if( access.isNotBlank() || access.isNotEmpty() ) {
                Log.d("application start","토큰을 재발급 받으려고 합니다..")
                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        reissueToken(username = username, refreshToken = refresh)
                    }
                }
            } else {
                Log.d("application start","로그인을 시도해야 합니다.")
                val intent = Intent(this, MemberActivity::class.java)
                startActivity(intent)
                finish()
            }
            Log.d("myIntent", intent.toString())
            NavController(intent)
        }
    }

    // 백그라운드 권한 설정
    @RequiresApi(Build.VERSION_CODES.O)
    fun requestAlertPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val alert = AlertDialog.Builder(this)
                alert.setTitle("권한 요청")
                alert.setMessage("설정한 알람이 동작하기 위해서는 다른앱 위에 표시 설정이 필요합니다.\n(변경된 구글 정책에 따른 동의요청)")
                alert.setPositiveButton("지금 설정") { dialog : DialogInterface?, _: Int ->
                    dialog!!.dismiss()
                    requestDrawOverlay()
                }
                alert.setNegativeButton("나중에") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                    finish() }
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

//    fun openGallery() {
//        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//        requireActivity().startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
//    }

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

    override fun onStart() {
        super.onStart()
        Log.d("MainActiviy","MainActivity 시작")
        val net = WifiUtil.isNetworkConnected(this.application)
        if(net.not()) {
            val intent = Intent(this,MemberActivity::class.java)
            startActivity(intent)
            finish()
            showToast(this,"네트워크 연결을 확인해주세요.")
        }
        Log.d("wifiUtil","MainActiviy 네트워크 연결상태 확인 : $net")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActiviy","MainActivity 정지")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActiviy","MainActivity 종료")
    }

}
