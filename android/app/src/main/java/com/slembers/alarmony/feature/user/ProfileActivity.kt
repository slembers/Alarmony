package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.slembers.alarmony.R
import com.slembers.alarmony.feature.screen.GroupActivity
import com.slembers.alarmony.MainActivity
import com.slembers.alarmony.network.repository.MemberService
import com.slembers.alarmony.feature.screen.MemberActivity
import com.slembers.alarmony.feature.ui.common.AnimationRotation
import com.slembers.alarmony.network.repository.MemberService.logOut
import com.slembers.alarmony.network.repository.MemberService.signout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.async

@Preview
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileSetting(navController: NavController = rememberNavController()) {
    // 이메일과 닉네임 정보를 가지고 있는 상태 변수
    var username = remember { mutableStateOf("아이디") }
    var nickname = remember { mutableStateOf("닉네임") }
    var email = remember { mutableStateOf("xxxx@naver.com") }

    // 프로필 이미지를 가지고 있는 상태 변수
    var profileImage = remember { mutableStateOf("") }

    // 닉네임 수정 모드를 제어하는 상태 변수
    var isEditMode = remember { mutableStateOf(false) }
    val context = LocalContext.current
    var mySelectedUri = remember { mutableStateOf<Uri>(Uri.EMPTY) }
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("AlarmListScreen","[알람목록] Activity 이동성공")
        }
    }

    LaunchedEffect(Unit) {
        val myInfo = MemberService.getMyInfo()

        username.value = myInfo?.username.toString()
        nickname.value = myInfo?.nickname.toString()
        Log.d("loadInfo", "${myInfo?.profileImgUrl}")
        Log.d("loadInfo", "${myInfo?.profileImgUrl.toString()}")
        profileImage.value = myInfo?.profileImgUrl.toString()
        email.value = myInfo?.email.toString()

    }

    fun changeNickname (changeName : String) {
        CoroutineScope(Dispatchers.IO).launch {
            isEditMode.value = !isEditMode.value
            Log.d("Info","닉네임 변경 할거임")
            val result = MemberService.modifyMemberNickname(changeName.trim('"'))

            if(result?.success == true) {
                showToast(context, "닉네임이 변경되었습니다.")
            } else {
                showToast(context, "닉네임이 이미 사용중입니다.")
                nickname.value = result?.nickname.toString()
            }
        }
    }


    // 로딩화면을 보여주는 변수
    val loading = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("계정설정") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row {

                    Column(modifier = Modifier.padding(16.dp))
                    {
                        // 프로필 이미지
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally),
                        ) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (profileImage.value.isEmpty() || profileImage.value == "null") {
                                    Image(
                                        painter = painterResource(R.drawable.mascot_foreground),
                                        contentDescription = "프로필 이미지",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clickable(onClick = {
                                                Log.d("profile", "기본 이미지")
                                            })
                                    )
                                    Log.d("profile", "기본 이미지 출력")
                                } else {
                                    AsyncImage(
                                        model = profileImage.value,
                                        contentDescription = "업로드 된 이미지",
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clickable(onClick = {
                                                Log.d("profile", "업로드된 이미지")
                                            })
                                    )
                                    Log.d("profile", "업로드된 이미지 출력")
                                }

                                // 이미지 수정 버튼
                                ImageUploader(onImageUploadComplete = { uri ->
                                    Log.d("image", "이미지 선택 완료")
                                    mySelectedUri.value = uri
//                                    isImageToBePaint.value = true

                                    CoroutineScope(Dispatchers.IO).launch {
                                        val result = MemberService.modifyMemberImage(
                                            imgProfileFile = uriToMultiPart(
                                                mySelectedUri.value,
                                                context
                                            )
                                        )
                                        if (result != null) {
                                            profileImage.value = result
                                        }
                                    }
                                })
                            }
                            Column(
                                Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            ) {
                                // 아이디
                                Text(
                                    text = username.value,
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 20.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Normal
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                // 이메일
                                Text(
                                    text = email.value,
                                    style = TextStyle(
                                        color = Color.Black,
                                        fontSize = 17.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Normal
                                    )

                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                // 닉네임
                                if (isEditMode.value) {
                                    // 수정 모드일 경우 TextField를 보여준다.
                                    OutlinedTextField(
                                        value = nickname.value,
                                        onValueChange = {
                                            nickname.value = it
                                            if (it.endsWith("\n")) {
                                                nickname.value = nickname.value.trim()
                                                changeNickname(nickname.value)

                                            }},
                                        modifier = Modifier.fillMaxWidth(),
                                        label = { Text("닉네임") },
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Email,
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                changeNickname(nickname.value)
                                            }
                                        )

                                        )
                                } else {
                                    // 수정 모드가 아닐 경우 Text를 보여준다.
                                    Row (
                                        verticalAlignment = Alignment.Bottom
                                            ){
                                        Text(
                                            text = nickname.value,
                                            style = MaterialTheme.typography.h6,
                                        )
                                        Text(
                                            text = "  (닉네임 변경)",
                                            style = MaterialTheme.typography.subtitle2.copy(color = Color.Blue),
                                            modifier = Modifier.clickable {
                                                isEditMode.value = !isEditMode.value
                                                if (!isEditMode.value) {
                                                    Log.d(
                                                        "upload",
                                                        mySelectedUri.value.toString()
                                                    )
                                                    // 수정 모드를 끝낼 때 서버에 닉네임 변경 요청을 보내는 로직
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
//                    TextButton(
//                        onClick = { logOut(context, navController) }
//                    ) {
                    TextButton(
                        enabled = !loading.value,
                        onClick = {
                            loading.value = true
                            CoroutineScope(Dispatchers.IO).async {
                                val result = logOut()
                                if( result ) {
                                    val intent = Intent(context, MemberActivity::class.java)
                                    context.startActivity(intent)
                                    (context as Activity).finish()
                                    loading.value = false
                                }
                                loading.value = false
                            }

                        }
                    ) {
                        Text(text = "로그아웃 |")
                    }

                    TextButton(onClick = { signout()
                    {isSuccess ->
                        if (isSuccess) {
                            Log.d("response","회원탈퇴 성공~~~")
                            Toast.makeText(context, "회원탈퇴 성공!", Toast.LENGTH_LONG).show()
                            val intent = Intent(context, MainActivity::class.java)
                            launcher.launch(intent)
                        } else {
                            Log.d("response","회원탈퇴 실패ㅜㅜ")
                            Toast.makeText(context, "회원탈퇴 실패...", Toast.LENGTH_LONG).show()
                        }
                    }
                    }
                    ) {
                        Text(
                            text = "회원탈퇴",
                            style = MaterialTheme.typography.body1.copy(color = Color.Red)
                        )
                    }

                }
            }

        }
    )
    if(loading.value) {
        AnimationRotation()
    }

}

fun showToast(context: Context, message: String) {
    CoroutineScope(Dispatchers.Main).launch {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun ImageUploader(onImageUploadComplete: (Uri) -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
//                val inputStream = context.contentResolver.openInputStream(it)
//                val bitmap = BitmapFactory.decodeStream(inputStream)
//                Log.d("image", "이미지 선택됨")
                onImageUploadComplete(uri)
                //uploadImage(bitmap, onImageUploadComplete)
            }
        }
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "이미지 변경",
            Modifier
                .padding(6.dp)
                .clickable { launcher.launch("image/*") },
            style = MaterialTheme.typography.body1.copy(color = Color.Blue)

        )
    }

}

fun uriToMultiPart(uri: Uri, context: Context): MultipartBody.Part {
//    val file = File(absolutelyPath(uri, context))
//    val file = File(uri?.path)
//    Log.d("file", file.toString())
//    Log.d("file", file.path)
//
//    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
//
//    return MultipartBody.Part.createFormData("proFile", file.name, requestFile)
    var file = UriUtil.toFile(context, uri)
    return FormDataUtil.getImageMultipart("imgProfileFile", file)
}


object FileUtil {
    // 임시 파일 생성
    fun createTempFile(context: Context, fileName: String): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(storageDir, fileName)
    }

    // 파일 내용 스트림 복사
    fun copyToFile(context: Context, uri: Uri, file: File) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        val buffer = ByteArray(4 * 1024)
        while (true) {
            val byteCount = inputStream!!.read(buffer)
            if (byteCount < 0) break
            outputStream.write(buffer, 0, byteCount)
        }

        outputStream.flush()
    }
}

object FormDataUtil {
    // File -> Multipart
    fun getImageMultipart(key: String, file: File): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = key,
            filename = file.name,
            body = file.asRequestBody("image/*".toMediaType())
        )
    }

    // String -> RequestBody
    fun getTextRequestBody(string: String): RequestBody {
        return string.toRequestBody("text/plain".toMediaType())
    }
}

object UriUtil {
    // URI -> File
    fun toFile(context: Context, uri: Uri): File {
        val fileName = getFileName(context, uri)

        val file = FileUtil.createTempFile(context, fileName)
        FileUtil.copyToFile(context, uri, file)

        return File(file.absolutePath)
    }

    // get file name & extension
    fun getFileName(context: Context, uri: Uri): String {
        val name = uri.toString().split("/").last()
        val ext = context.contentResolver.getType(uri)!!.split("/").last()

        return "$name.$ext"
    }
}


//TextField(
//value = nickname,
//onValueChange = {
//    nickname = it
//
//},
//keyboardOptions = KeyboardOptions(
//keyboardType = KeyboardType.Email,
//imeAction = ImeAction.Done
//),
//
//modifier = Modifier
//.fillMaxWidth()
//.onFocusChanged { nicknameFocused = if (it.isFocused) true else false
//    Log.d("response", "닉네임 포커스${nicknameFocused}")
//}
//.fillMaxWidth(),
//
//label = { Text(text = "닉네임") },
//singleLine = true,
//maxLines = 1,
//)

//if (!nicknameregex.matches(nickname)) {
//    Log.d("response", "닉네임 포커스${nicknameFocused}")
//    Log.d("response", "${nickname}")
//    Log.d("response", "정규표현식 통과 못함")
//
//    Text(
//        text = "닉네임은 2~10자의 문자와 숫자로 만들어주세요!",
////                        color = Color.Red,
//        fontSize = 12.sp,
//        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
//    )
//
//} else if (nicknameFocused == true) {
//    MemberService.checkNickname(nickname) { isSuccess ->
//        nicknameResult = isSuccess
//        Log.d("response", "${nicknameResult}")
//        if (nicknameResult == true) {
//            duplicatedNickname = true
//        } else {
//            duplicatedNickname = false
//        }
//    }
//}
//
//if (duplicatedNickname == true) {
//    Text(
//        text = "중복된 닉네임입니다.",
////                        color = Color.Red,
//        fontSize = 12.sp,
//        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
//    )
//}