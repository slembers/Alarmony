package com.slembers.alarmony

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        printRegistrationToken()
    }

    /**
     * 기기에 등록된 토큰을 출력한다.
     */
    @SuppressLint("StringFormatInvalid")
    private fun printRegistrationToken() {
        //토큰 출력
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                println("FCM 등록 토큰: $token")
            } else {
                println("등록 토큰 가져오기 실패")
            }
        }
    }
}