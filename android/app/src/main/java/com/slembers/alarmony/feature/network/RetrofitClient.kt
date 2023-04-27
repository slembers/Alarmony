package com.slembers.alarmony.feature.network


import android.content.Context
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST


//data class LoginResponse(
//    val result: Boolean,
//    val message: String,
//    val data: LoginData?
//)
data class LoginResponse(val data: String?)


data class LoginData(
    val id: String,
    val password: String
)


public interface ApiService {
//    헤더를 붙인다면 아래 방식으로 준다.
    @Headers("Content-Type: application/json")
    @POST("login")
    fun login(
        @Field("id") id: String,
        @Field("password") password: String
//        @Body request: LoginData
    ): Call<LoginResponse>

}


object RetrofitClient {
    private const val BASE_URL = "http://localhost:5000/"

    private fun getRetrofitInstance(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(context: Context): ApiService {
        return getRetrofitInstance(context).create(ApiService::class.java)
    }
}
