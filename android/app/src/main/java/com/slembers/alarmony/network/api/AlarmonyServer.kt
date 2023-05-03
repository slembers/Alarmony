package com.slembers.alarmony.network.api

import androidx.compose.material3.ExperimentalMaterial3Api
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.gson.GsonBuilder
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.network.repository.GroupRepositroy
import com.slembers.alarmony.network.repository.MemberRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AlarmonyServer {

    val gson = GsonBuilder().serializeNulls().create()

//    private const val BASE_URL = "http://192.168.216.18:5000/api/"
    private const val BASE_URL = "https://k8c205.p.ssafy.io/api/"

    private fun okHttpClient(interceptor : Appinterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val retrofit : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient(Appinterceptor()))
        .build()

    val memberApi = retrofit.create(MemberRepository::class.java)
    val groupApi = retrofit.create(GroupRepositroy::class.java)

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    class Appinterceptor : Interceptor {
        val accessToken = MainActivity.prefs.getString("accessToken","")
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build()
            proceed(newRequest)
        }
    }
}