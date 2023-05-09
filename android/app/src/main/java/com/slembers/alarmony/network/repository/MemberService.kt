package com.slembers.alarmony.network.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.gson.Gson
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.model.db.FindIdRequest
import com.slembers.alarmony.model.db.FindPasswordRequest
import com.slembers.alarmony.model.db.LoginRequest
import com.slembers.alarmony.model.db.SignupRequest
import com.slembers.alarmony.model.db.dto.FindIdResponseDto
import com.slembers.alarmony.model.db.dto.FindPasswordResponseDto
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
                    Log.d("response","response")
//                    code
                    Log.d("response","${response.code()}")
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




//    email을 보내고 이후의 동작은 추후에 확인
    fun findId(email: String) {
      try {
          Log.d("test","아이디 찾기 위해 이메일 보냄")
          memberApi.findId(
             FindIdRequest(
                 email = email
             )
          ).enqueue(object : Callback<FindIdResponseDto> {
              override fun onResponse(
                  call: Call<FindIdResponseDto>,
                  response: Response<FindIdResponseDto>) {
                  Log.d("response","확인")
                  Log.d("response","${call}")
                  Log.d("response", "${response.body()}")
                  Log.d("response","${response.code()}")
                  var findIdResult = response.body();

              }
//서버 요청이 실패한 경우 호출된다.
              override fun onFailure(call: Call<FindIdResponseDto>, t: Throwable, ) {
                  Log.d("response", "서버 요청이 실패")
                  Log.d("response", "${t}")

              }
          }

          )
//          try에서 예외가 발생하면 호춯된다.
      } catch ( e: Exception ) {
          Log.d("response", "예외발생")
      }
    }




    @OptIn(ExperimentalGlideComposeApi::class)
    @ExperimentalMaterial3Api
    fun login(
        username: String,
        password:String,
        navController: NavController,
        context: Context,
//        아래는 login을 import한 composable함수에서 데이터를 사용하기 위해 callback함수로 건네주는 데이터들
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
                    Log.d("response1","${response}")
                    Log.d("response","${response.body()}")


                    Log.i("response", "${loginResult}")
                    Log.i("accessToken", "${loginResult?.accessToken}")
//로그인이 성공했을 경우 로직
                    if(loginResult!!.status == null) {
                        Log.d("response", "로그인 성공!")
                        Log.d("response", "${loginResult.accessToken}")
//                        임시방편
                        MainActivity.prefs.setBoolean("auto_login", true)
                        navController.navigate(NavItem.AlarmListScreen.route)


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

                    } else if(loginResult.status!! == "404") {
                        Log.d("response","회원이 존재하지 않음.")
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

    @ExperimentalMaterial3Api
    fun autoLogin(
        username: String,
        password:String,
//        아래는 login을 import한 composable함수에서 데이터를 사용하기 위해 callback함수로 건네주는 데이터들
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

                }
            })

        } catch ( e : Exception ) {
            println(e.message)


        }
        Log.d("Exit", "login <-- 로그인 종료")
    }


    fun findPswd(
        email: String,
        username: String,
    ) {
        try {
            memberApi.findPassword(
                FindPasswordRequest(
                    username = username,
                    email = email,
                )

            ).enqueue(object: Callback<FindPasswordResponseDto> {
                override fun onResponse(call: Call<FindPasswordResponseDto>, response: Response<FindPasswordResponseDto>) {
                    if(response.isSuccessful) {
                        Log.d("response", "비밀번호찾기 신호 성공")
                    } else {
                        Log.d("response", "비밀번호찾기 신호 실패")

                    }
                }

                override fun onFailure(call: Call<FindPasswordResponseDto>, t: Throwable) {
                    Log.d("fail", "비밀번호찾기 실패")
                }
            })
        } catch ( e : Exception ) {
            Log.d("fail", "비밀먼호 찾기 예외 발생")
            println(e.message)
        }

    }
}

