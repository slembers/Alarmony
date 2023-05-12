package com.slembers.alarmony.network.api

import androidx.compose.material3.ExperimentalMaterial3Api
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.gson.GsonBuilder
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.feature.notification.NotiService
import com.slembers.alarmony.network.repository.GroupRepositroy
import com.slembers.alarmony.network.repository.MemberRepository
import com.slembers.alarmony.network.repository.ReportRepository
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AlarmonyServer {

    val gson = GsonBuilder().setLenient().create()

//    private val BASE_URL = "http://10.0.2.2:5000/api/"
    private val BASE_URL = "https://k8c205.p.ssafy.io/api/"

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
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient(Appinterceptor()))
        .build()

    val notiApi = retrofit.create(NotiService::class.java)
    val memberApi = retrofit.create(MemberRepository::class.java)
    val groupApi = retrofit.create(GroupRepositroy::class.java)
    val reportApi = retrofit.create(ReportRepository::class.java)

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    class Appinterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val accessToken : String? = MainActivity.prefs.getString("accessToken","")
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
            proceed(newRequest)
        }
    }
}