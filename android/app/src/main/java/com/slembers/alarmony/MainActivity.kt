package com.slembers.alarmony

import android.content.ContentValues
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
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
        }
    }

    // 액티비티간 데이터를 주고 받기 위함
    @RequiresApi(Build.VERSION_CODES.M)
    val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d(ContentValues.TAG, "Request permission: " + result.resultCode)
        }
}