package com.slembers.alarmony.util

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import com.slembers.alarmony.R

class MessageActivity : AppCompatActivity() {
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