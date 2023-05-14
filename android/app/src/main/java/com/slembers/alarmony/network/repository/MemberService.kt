package com.slembers.alarmony.network.repository

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.MainActivity.Companion.prefs
import com.slembers.alarmony.feature.common.NavItem
import com.slembers.alarmony.feature.ui.common.showDialog
import com.slembers.alarmony.model.db.ChangeNicknameRequestDto
import com.slembers.alarmony.model.db.FindIdRequest
import com.slembers.alarmony.model.db.FindPasswordRequest
import com.slembers.alarmony.model.db.LoginRequest
import com.slembers.alarmony.model.db.ModifyMemberInfoDto
import com.slembers.alarmony.model.db.RegistTokenDto
import com.slembers.alarmony.model.db.SignupRequest
import com.slembers.alarmony.model.db.TokenReissueRequest
import com.slembers.alarmony.model.db.dto.CheckEmailResponseDto
import com.slembers.alarmony.model.db.dto.CheckIdResponseDto
import com.slembers.alarmony.model.db.dto.CheckNicnameResponseDto
import com.slembers.alarmony.model.db.dto.FindIdResponseDto
import com.slembers.alarmony.model.db.dto.FindPasswordResponseDto
import com.slembers.alarmony.model.db.dto.GetMyInfoDto
import com.slembers.alarmony.model.db.dto.NicknameResponseDto
import com.slembers.alarmony.model.db.dto.SignupResponseDto
import com.slembers.alarmony.network.api.AlarmonyServer
import org.json.JSONObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@ExperimentalMaterial3Api
@ExperimentalGlideComposeApi
object MemberService {
    val memberApi = AlarmonyServer().memberApi

    suspend fun putRegistTokenAfterSignIn(): Unit {
        Log.d("INFO", "동적으로 가져오는 토큰 정보")
        val token = MainActivity.prefs.getString("registrationToken", "")
        if (token.isNotBlank()) {
            putRegistToken(token)
        } else {
            Log.d("INFO", "등록 토큰이 존재하지 않습니다.")
        }
    }

    suspend fun putRegistToken(token: String?) {
        try {
            Log.d("INFO", "함수 도달 성공")
            val tt: String? = MainActivity.prefs.getString("accessToken", "")
            Log.d("INFO", "$tt")
            memberApi.putRegistToken(
                RegistTokenDto(
                    registrationToken = token
                )
            )
            Log.i("response", "토큰이 전송되었습니다.")
        } catch (e: Exception) {
            Log.d("disconnection", "등록토큰이 전송되지 못했습니다.")
            println(e.message)
        }
    }

    fun signout(
        isSuccess: (Boolean) -> Unit = {}

    ) {
        try {
            Log.d("response", "signout--화원탈퇴 시도")
            memberApi.signOut()
                .enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful) {
                            Log.d("response", "회원탈퇴 성공${response}")
                            isSuccess(true)
                            prefs.reset()

                        } else {
                            Log.d("response", "회원탈퇴 실패1${response}")
                            Log.d("response", "회원탈퇴 실패1${response.body()}")
                            Log.d("response", "회원탈퇴 실패1${response.errorBody()}")
                            isSuccess(false)
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Log.d("disconnection", "회원탈퇴 실패하였습니다..")
                        Log.d("disconnection", "회원탈퇴 원인 : ${t.message}..")
                        isSuccess(false)
                    }

                })
        } catch (e: Exception) {
            Log.d("response", "회원탈퇴 아예안됨?")
            println(e.message)
        }
    }

    fun signup(
        request: SignupRequest,
        isSuccess: (Boolean) -> Unit = {}
    ) {
        try {
            Log.d("가입", "signup ==> 회원가입시도")
            memberApi.signup(request).enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    Log.d("response", "response")
                    Log.d("response", "${response.code()}")
                    if (response.isSuccessful) {
                        Log.d("success", "회원가입 성공!!!")
//                         부모 객체에 "성공"이라는 string을 건네줘야한다. 즉 회원가입이 성공했음을 알려야한다. 어떻게??
                        isSuccess(true)
                        Log.d("success", "${isSuccess}")
                    } else {
                        Log.d("Failed", "회원가입 실패")
                        isSuccess(false)
                    }
                }
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("disconnection", "회원가입 실패하였습니다..")
                    Log.d("disconnection", "회원가입 원인 : ${t.message}..")
                    isSuccess(false)
                }
            })
        } catch (e: Exception) {
            Log.d("response", "회원가입 아예안됨?")
            println(e.message)
        }
    }


//    email을 보내고 이후의 동작은 추후에 확인


    @OptIn(ExperimentalGlideComposeApi::class)
    @ExperimentalMaterial3Api
    suspend fun login(
        username: String,
        password: String,
        context: Context
    ): Boolean {


        Log.d("Start", "login --> 로그인 시도")
        val response = memberApi.login(
            LoginRequest(
                username = username,
                password = password
            )
        )

        //로그인이 성공하였을경우 -> SUCCESS 200번대
        if (response.isSuccessful) {

            var loginResult = response.body()
            //sharedPreference에 저장
            MainActivity.prefs.setString("accessToken", loginResult?.accessToken)
            MainActivity.prefs.setString("refreshToken", loginResult?.refreshToken)
            MainActivity.prefs.setString("username", username)

            Log.d("login", "[로그인] 성공!")

            return true;
        }
        //로그인이 실패하였을 경우 400번대 에러
        else {
            val duration = Toast.LENGTH_SHORT
            var message = ""

            val jObjError = JSONObject(response.errorBody()!!.string())
            val status = jObjError.getString("status")

            when (status) {
                "401" -> {
                    message = "아이디와 비밀번호를 다시 확인해주세요."
                }

                "403" -> {
                    message = "회원가입 이메일 인증을 완료해주세요"
                }

                "404" -> {
                    message = "가입되지 않은 회원입니다."
                }

                else -> {
                    message = "내부 서버에 문제가 발생하였습니다. 잠시 후에 다시 시도해주세요"
                }

            }
            val toast = Toast.makeText(context, message, duration)
            toast.setGravity(Gravity.TOP, 0, 0)
            toast.show()
            return false;
        }
    }

    fun findId(email: String, context: Context, navController: NavController) {
        try {
            Log.d("test", "아이디 찾기 위해 이메일 보냄")
            memberApi.findId(
                FindIdRequest(
                    email = email
                )
            ).enqueue(object : Callback<FindIdResponseDto> {
                override fun onResponse(
                    call: Call<FindIdResponseDto>,
                    response: Response<FindIdResponseDto>
                ) {
                    Log.d("response", "${response.body()}")
                    Log.d("response", "${response.code()}")
                    Log.d("response", "확인")
                    Log.d("response", "${call}")
//                    retrofit에서 제공하는 response의 isSuccessful값이 true라면 아래 실행
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context,
                            "해당 이메일로 아이디 찾기 안내 메일을 전송하였습니다.",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(NavItem.LoginScreen.route)

                    } else {
                        Toast.makeText(
                            context,
                            "가입되지 않은 이메일 입니다.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                //서버 요청이 자체가 실패한 경우 아래 onFailure가 실행된다.
                override fun onFailure(call: Call<FindIdResponseDto>, t: Throwable) {
                    showDialog("알림", "뭔가 잘못됐나봐요", context, navController)
                    Log.d("response", "서버 요청이 실패")
                    Log.d("response", "${t}")
                }
            }

            )
//          try에서 예외가 발생하면 호춯된다.
        } catch (e: Exception) {
            Log.d("response", "예외발생")
            showDialog("알림", "뭔가 잘못됐나봐요", context, navController)
        }
    }

    fun findPswd(
        email: String,
        username: String,
        context: Context,
        navController: NavController
    ) {
        try {
            memberApi.findPassword(
                FindPasswordRequest(
                    username = username,
                    email = email,
                )

            ).enqueue(object : Callback<FindPasswordResponseDto> {
                override fun onResponse(
                    call: Call<FindPasswordResponseDto>,
                    response: Response<FindPasswordResponseDto>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context,
                            "해당 이메일로 임시 비밀번호를 발급하였습니다.",
                            Toast.LENGTH_LONG
                        ).show()
                        navController.navigate(NavItem.LoginScreen.route)
                    } else {
                        Toast.makeText(
                            context,
                            "올바른 정보를 입력해주세요",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                override fun onFailure(call: Call<FindPasswordResponseDto>, t: Throwable) {
                    Log.d("fail", "비밀번호찾기 실패")
                    showDialog("알림", "뭔가 잘못됐나봐요", context, navController)
                }
            })
        } catch (e: Exception) {
            Log.d("fail", "비밀먼호 찾기 예외 발생")
            showDialog("알림", "뭔가 잘못됐나봐요", context, navController)
            println(e.message)
        }
    }

    //    @OptIn(ExperimentalGlideComposeApi::class)
    @ExperimentalMaterial3Api
    suspend fun logOut(): Boolean {
        try {
            val response = memberApi.logOut()
            Log.d("response", "로그아웃")
            Log.d("response", "${response.body()}")
            Log.d("response", "${response.code()}")
            MainActivity.prefs.reset()
            return response.code() in 200..300
        } catch (e: Exception) {
            Log.d("fail", "로그아웃 예외 발생")
            return false
        }
//        fun signOut(context:Context, navController: NavController)
    }

    fun checkId(
        username: String,
        isSuccess: (Boolean) -> Unit = {}
    ) {
        try {
            memberApi.checkId(

                username = username
            ).enqueue(object : Callback<CheckIdResponseDto> {
                override fun onResponse(
                    call: Call<CheckIdResponseDto>,
                    response: Response<CheckIdResponseDto>
                ) {
//                    엄밍히 말하면 모든 응답이 성공으로 들어온다.
                    if (response.isSuccessful) {
                        Log.d("response", "아이디체크")
                        Log.d("response", "${username}")
                        Log.d("response", "${response.body()}")
                        if (response.body()?.duplicated == true) {
                            isSuccess(true)
                        } else {
                            isSuccess(false)
                        }

                    } else {
                        Log.d("response", "아이디체크 실패")

                    }
                }

                override fun onFailure(call: Call<CheckIdResponseDto>, t: Throwable) {
                    Log.d("fail", "실패")
                }
            })
        } catch (e: Exception) {
            Log.d("fail", "실패2")

        }
    }

    fun checkEmail(
        email: String,
        isSuccess: (Boolean) -> Unit = {}
    ) {
        try {
            memberApi.checkEmail(

                email = email
            ).enqueue(object : Callback<CheckEmailResponseDto> {
                override fun onResponse(
                    call: Call<CheckEmailResponseDto>,
                    response: Response<CheckEmailResponseDto>
                ) {
                    if (response.isSuccessful) {
                        Log.d("response", "이메일체크통신")
                        Log.d("response", "${response.body()}")
                        if (response.body()?.duplicated == true) {
                            isSuccess(true)
                        } else {
                            isSuccess(false)
                        }

                    } else {
                        Log.d("response", "이메일 실패")

                    }
                }

                override fun onFailure(call: Call<CheckEmailResponseDto>, t: Throwable) {
                    Log.d("fail", "실패")
                }
            })
        } catch (e: Exception) {
            Log.d("fail", "실패2")

        }
    }

    fun checkNickname(
        nickname: String,
        isSuccess: (Boolean) -> Unit = {}
    ) {
        try {
            memberApi.checkNickname(

                nickname = nickname
            ).enqueue(object : Callback<CheckNicnameResponseDto> {
                override fun onResponse(
                    call: Call<CheckNicnameResponseDto>,
                    response: Response<CheckNicnameResponseDto>
                ) {
                    if (response.isSuccessful) {
                        Log.d("response", "닉네임체크")
                        Log.d("response", "${nickname}")
                        Log.d("response", "${response.body()}")
                        if (response.body()?.duplicated == true) {
                            isSuccess(true)
                        } else {
                            isSuccess(false)
                        }

                    } else {
                        Log.d("response", "닉네임 실패")

                    }
                }

                override fun onFailure(call: Call<CheckNicnameResponseDto>, t: Throwable) {
                    Log.d("fail", "실패")
                }
            })
        } catch (e: Exception) {
            Log.d("fail", "실패2")

        }
    }

    /**
     * 토큰 재발급
     */
    suspend fun reissueToken(username: String, refreshToken: String): Boolean {

        Log.d("refresh", "토큰 재발급")
        // 토큰 재발급 API 호출
        val response = memberApi.refresh(
            TokenReissueRequest(
                grantType = "Bearer",
                username = username,
                refreshToken = refreshToken,
            )
        )
        //성공 200~300번
        //성공 시에는 재발급 받은 토큰을 sharedPreference에 저장한다.
        if (response.isSuccessful) {
            Log.d("refresh", "액세스 토큰" + response.body()?.accessToken)
            Log.d("refresh", "리프레시 토큰" + response.body()?.refreshToken)

            MainActivity.prefs.setString("accessToken", response.body()?.accessToken)
            MainActivity.prefs.setString("refreshToken", response.body()?.refreshToken)

            return true;
        } else {
            //실패 시에는 로그인 만료 메세지를 보내주고 로그아웃 시킨다.
            return false
        }
        //실패 400번
    }

    fun modifyMemberInfo(
        nickname: String,
        imgProfileFile: MultipartBody.Part
    ) {
        try {
            memberApi.modifyMemberInfo(
                ModifyMemberInfoDto(
                    nickname = nickname,
                    imgProfileFile = imgProfileFile
                )
            ).enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    if (response.isSuccessful) {
                        Log.d("response", " 정보 수정 성공")
                    } else {
                        Log.d("response", " 내부 로직에서 실패")
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.d("response", "실패 : $t")
                }
            })
        } catch (e: Exception) {
            Log.d("response", " 실패 : $e")
        }
    }

    suspend fun modifyMemberImage(
        imgProfileFile: MultipartBody.Part
    ): String? {
        return try {
            val imageResponseDto = memberApi.modifyMemberImage(
                imgProfileFile = imgProfileFile
            )
            Log.d("changeImage", "이미지 정보 : ${imageResponseDto.body()}.")
            imageResponseDto.body()?.profileImgUrl
        } catch (e: Exception) {
            Log.d("response", " 이미지 재로딩 실패 : $e")
            null
        }
    }

    suspend fun modifyMemberNickname(
        changeName: String
    ): NicknameResponseDto? {
        return try {
            val nicknameResponseDto = memberApi.modifyMemberNickname(
                ChangeNicknameRequestDto(
                    changeName = changeName
                )
            )
            Log.d("changeName", "닉네임 요청 정보 : ${nicknameResponseDto.body()?.nickname}.")
            nicknameResponseDto.body()
        } catch (e: Exception) {
            Log.d("response", " 닉네임 요청 실패 : $e")
            null
        }
    }

    suspend fun getMyInfo(): GetMyInfoDto? {
        return try {
            val myInfo = memberApi.getMyInfo()
            Log.d("getmyinfo", "내 정보 : ${myInfo.body()}.")
            myInfo.body()
        } catch (e: Exception) {
            Log.d("getmyinfo", "내 정보 가져오기에서 오류가 발생했습니다.")
            null
        }
    }
}

