package com.slembers.alarmony.network.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import com.google.gson.Gson
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.model.db.LoginRequest
import com.slembers.alarmony.model.db.SignupRequest
import com.slembers.alarmony.model.db.dto.LoginResponseDto
import com.slembers.alarmony.model.db.dto.SignupResponseDto
import com.slembers.alarmony.network.api.AlarmonyServer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object MemberService {
    val memberApi = AlarmonyServer.memberApi

    fun singup(username: String, password: String, nickname: String, email: String) {
        try {
            Log.d("가입", "signup ==> 회원가입시도")
            memberApi.signup(
                SignupRequest(
                    username = username,
                    password = password,
                    nickname =nickname,
                    email = email
                )
            ).enqueue(object : Callback<SignupResponseDto> {
                override fun onResponse(call: Call<SignupResponseDto>, response: Response<SignupResponseDto>) {
                    Log.i("response","response")
                    Log.i("response","${response.body()}")
                    if(response.isSuccessful) {
                        Log.d("success", "회원가입 성공")
                    } else {
                        Log.d("Failed", "회원가입 실패")
                    }
                }

                override fun onFailure(call: Call<SignupResponseDto>, t: Throwable) {
                    Log.d("disconnection", "회원가입 실패하였습니다..")
                }
            })
        } catch ( e : Exception ) {
            Log.d("response", "회원가입 아예안됨?")
            println(e.message)
        }
    }

    fun findpswd(username:String, email: String ) {

    }

    fun findid(email: String, authneum:Int) {

    }





    fun login(
        username: String,
        password:String,
        navController: NavController,
        context: Context,
        resultCallback: (resultText: String, accessToken: String?, refreshToken: String? ) -> Unit
    ) {
        var resultText = ""

        try {
            Log.d("Start", "login --> 로그인 시도")
            memberApi.login(
                LoginRequest(
                        username = username,
                        password = password
                )
            ).enqueue(object : Callback<LoginResponseDto> {
                override fun onResponse(call: Call<LoginResponseDto>, response: Response<LoginResponseDto>) {
                    var loginResult = response.body();


                    Log.i("response", "${loginResult}")
                    Log.i("accessToken", "${loginResult?.accessToken}")
//로그인이 성공했을 경우 로직
                    if(loginResult!!.status == null) {
                        Log.d("response", "로그인 성공!")
                        Log.d("response", "${loginResult.accessToken}")
                        //                        토큰값 저장해야함
                        navController.navigate(NavItem.AccountMtnc.route)

                        resultText = "로그인 성공"
                        resultCallback(resultText, loginResult.accessToken, loginResult.refreshToken)
//                        토큰을 저장하는 코드 삽입

                    } else if(loginResult!!.status!! == "401") {

                        Log.d("response","비밀번호가 일치하지 않는다.")
//                        팝업으로 알려주기
//                        유저아이디와 비밀번호 입력창 초기화
                        resultText = "비밀번호를 확인해주세요."
                        resultCallback(resultText, loginResult.accessToken, loginResult.refreshToken)

                    } else if(loginResult!!.status!! == "403") {
                        Log.d("response","이메일 정보를 확인해 주세요.")
                        resultText = "이메일을 확인해주세요."
                        resultCallback(resultText, loginResult.accessToken, loginResult.refreshToken)

                    } else if(loginResult!!.status!! == "404") {
//                        Log.d("response","회원이 존재하지 않음.")
                        resultText = "회원이 존재하지 않습니다."
                        resultCallback(resultText, loginResult.accessToken, loginResult.refreshToken)
                        Log.d("response",resultText)
                    }

                    Log.d("test", "여기까지옴")




                }


                override fun onFailure(call: Call<LoginResponseDto>, t: Throwable) {
                    Log.i("", "로그인 실패하였습니다..")
//                    토스트는 어차피 화면단에 보이는 거니까 로직만 서비스에 보내고 토스트는 액티비티에서 띄우기
                    Toast.makeText(context,"로그인에 실패했어요...", Toast.LENGTH_SHORT).show()
                }
            })

        } catch ( e : Exception ) {
            println(e.message)
            Toast.makeText(context,"로그인에 실패했어요...", Toast.LENGTH_SHORT).show()

        }
        Log.d("Exit", "login <-- 로그인 종료")
    }
}