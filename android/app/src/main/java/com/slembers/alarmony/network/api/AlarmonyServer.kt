package com.slembers.alarmony.network.api

import com.google.firebase.encoders.annotations.Encodable.Field
import com.google.gson.GsonBuilder
import com.slembers.alarmony.network.repository.MemberRepository
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AlarmonyServer {

    val gson = GsonBuilder().serializeNulls().create()

//    private const val BASE_URL = "http://192.168.0.102:5000/api/"
    private const val BASE_URL = "https://k8c205.p.ssafy.io/api/"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val memberApi = retrofit.create(MemberRepository::class.java)
}