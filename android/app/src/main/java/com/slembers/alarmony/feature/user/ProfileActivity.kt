package com.slembers.alarmony.feature.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
//import com.slembers.alarmony.Manifest
import com.slembers.alarmony.R
import com.slembers.alarmony.network.repository.MemberService.getMyInfo
import com.slembers.alarmony.network.repository.MemberService.logOut
import retrofit2.http.Multipart
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.slembers.alarmony.network.repository.MemberService.userProfoileEditSubmit
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


//fun 임시(context: Context) {
//
//    val permission = Manifest.permission.READ_EXTERNAL_STORAGE
//    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
//        // 권한이 없을 때 권한 요청 다이얼로그 띄우기
//        if (ActivityCompat.shouldShowRequestPermissionRationale(permission)) {
//            // 권한 요청 다이얼로그 표시
//            AlertDialog.Builder(context)
//                .setMessage("갤러리에 접근하기 위해서는 저장소 읽기 권한이 필요합니다.")
//                .setPositiveButton("OK") { dialog, _ ->
//                    // 권한 요청 다이얼로그 OK 버튼 클릭시 권한 요청하기
//                    ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), REQUEST_CODE_READ_EXTERNAL_STORAGE)
//                    dialog.dismiss()
//                }
//                .setNegativeButton("Cancel") { dialog, _ ->
//                    dialog.dismiss()
//                }
//                .show()
//        } else {
//            // 권한 요청하기
//            ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), REQUEST_CODE_READ_EXTERNAL_STORAGE)
//        }
//    } else {
//        // 권한이 있으면 갤러리 열기
//        openGallery()
//    }
//}

@Composable
fun RequestContentPermission() {

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileSetting(navController: NavController) {



    // 이메일과 닉네임 정보를 가지고 있는 상태 변수
    var nicknamePro = remember { mutableStateOf("닉네임") }
    var emailPro = remember { mutableStateOf("xxxx@naver.com") }
    var usernamePro = remember { mutableStateOf("유저네임") }
    // 프로필 이미지를 가지고 있는 상태 변수
//    따라서, 클라이언트 측에서는 이미지 파일의 경로나 URI를 주고받아야 한다.
//    서버 측에서는 이를 멀티파트 데이터로 변환해서 전송해야 합니다.
// 서버에서 건네준 uri, 받을땐느 이걸로 받자
    var profileImagePro = remember { mutableStateOf(null) }
//    사실 특수한 경우를 제외하면 베이직 이미지가 바뀌는 일은 없다.
    var basicProfileImage = remember { mutableStateOf(R.drawable.mascot_foreground) }

    // 닉네임 수정 모드를 제어하는 상태 변수
    var isEditMode = remember { mutableStateOf(false) }
//    var changedNickname = remember { mutableStateOf("") }
//    var changedProfileImage = remember { mutableStateOf<Bitmap?>(null) }


    var imageUri = remember {
        mutableStateOf<Uri?>(null)
    }

    val context = LocalContext.current


//    화면에 이미지를 표시하기 위한 변수
    val bitmap =  remember {
        mutableStateOf<Bitmap?>(null)
    }
//이건 무슨함수인가??
//    ActivityResultContracts.GetContent()는
//            이미지, 비디오, 오디오, 문서 등의 "컨텐츠"를 선택하는 데 사용되는 일반적인 ActivityResultContract입니다.
//    이 경우 uri는 선택된 이미지의 Uri입니다.
//    따라서 imageUri는 Uri 유형의 mutableStateOf입니다.
//    해당 변수에 다른 유형의 값 (예 : String)을 할당하려고하면 컴파일러가 유형 불일치 오류를 발생시킵니다.
//    따라서 imageUri를 String으로 정의하려면 mutableStateOf<String?>(null)과 같이 유형을 변경해야합니다.
//    이 경우 imageUri는 nullable String입니다.

//    이미지를 선택해서 uri값으로 변환해주는 코드
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri.value = uri

    }

    Column() {

        Spacer(modifier = Modifier.height(12.dp))

        imageUri.value?.let {
            if (Build.VERSION.SDK_INT < 28) {
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver,it)
//현재 프로젝트는 33버전이므로 아래코드가 실행된다.
            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver,it)
//     화면에 출력하기위해 비트맵 형식을 쓴다지만, 서버에 보낼떄는 multipart형식으로 보내야 한다.
                bitmap.value = ImageDecoder.decodeBitmap(source)

            }

            bitmap.value?.let {  btm ->
                Image(bitmap = btm.asImageBitmap(),
                    contentDescription =null,
                    modifier = Modifier.size(400.dp))
            }
        }

    }

//폼데이터 선언
    data class MyFormData(
        val nickname: String = "",
        val profileImage: MultipartBody.Part? = null
    )
    val myFormData = remember { mutableStateOf(MyFormData()) }

//    fun onChangeNickname(newNickname: String) {
//        myFormData.value = myFormData.value.copy(nickname = newNickname)
//    }



//    newImageUri는 갤러리에서 선택한 이미지
    fun onChangeProfileImage(newImageUri: Uri) {
    val imageStream = context.contentResolver.openInputStream(newImageUri)
        val imageBytes = imageStream?.readBytes()
        val requestBody = imageBytes?.toRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = requestBody?.let { MultipartBody.Part.createFormData("profileImage", "image.jpg", it) }
        myFormData.value = myFormData.value.copy(profileImage = imagePart)
//    갤러리에서 선택한 이미지를 폼데이터에 넣는다.
    }



    getMyInfo(
        context,
        navController,
        username = { usernamePro.value = it ?: "default value" },
        email = { emailPro.value = it ?: "default value" },
        profileImage = { profileImagePro.value = null },
        nickname = { nicknamePro.value = it ?: "default value" },




    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("계정설정") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp()}) {
                        Icon(Icons.Filled.ArrowBack, "뒤로가기")
                    }
                },
                backgroundColor = MaterialTheme.colors.primary
            )
        },
        content = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

//갤러리에서 불러온 이미지
                imageUri.value?.let {
                    if (Build.VERSION.SDK_INT < 28) {
                        bitmap.value = MediaStore.Images
                            .Media.getBitmap(context.contentResolver,it)

                    } else {
                        val source = ImageDecoder
                            .createSource(context.contentResolver,it)
                        bitmap.value = ImageDecoder.decodeBitmap(source)
                    }

                    bitmap.value?.let {  btm ->
                        Image(bitmap = btm.asImageBitmap(),
                            contentDescription =null,
                            modifier = Modifier.size(400.dp))
                    }
                }

                // 프로필 이미지
//                이미지가 없으면 drawable 형식의 기본이미자, 아니라면 multipart이미지
                if (profileImagePro.value == null) {
                    Image(
                        painter = painterResource(basicProfileImage.value),
                        contentDescription = "프로필 이미지",
                        modifier = Modifier
                            .size(100.dp)
                            .clickable(onClick = {  launcher.launch("image/*") })
                    )

                } else {
                    Log.d("response", "이미지${profileImagePro}")
                    Text("기본이 아닌 유저프로필 사진")
                          bitmap.value?.let {  btm ->
                        Image(bitmap = btm.asImageBitmap(),
                            contentDescription =null,
                            modifier = Modifier.size(400.dp))
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))

                // 이메일
                Text(
                    text = emailPro.value,
                    style = MaterialTheme.typography.subtitle1,


                )

                Spacer(modifier = Modifier.height(16.dp))

                // 닉네임
                if (isEditMode.value) {
                    // 수정 모드일 경우 TextField를 보여준다.
                    OutlinedTextField(
                        value = nicknamePro.value,
                        onValueChange = { nicknamePro.value = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("닉네임") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),

                    )
                } else {
                    // 수정 모드가 아닐 경우 Text를 보여준다.
                    Text(
                        text = nicknamePro.value,
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 닉네임 수정 버튼
                Button(
                    onClick = {
                        isEditMode.value = !isEditMode.value
                        if (!isEditMode.value) {
                            // 수정 모드를 끝낼 때 서버에 닉네임 변경 요청을 보내는 로직
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (isEditMode.value) "완료" else "수정",
                        style = MaterialTheme.typography.button
                    )
                }


                Row(
                    modifier = Modifier
//            .fillMaxWidth(),
                    ,

                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TextButton(onClick = {
                        logOut(context, navController)})


                    {
                        Text(text = "로그아웃 |")
                    }

                    TextButton(onClick = { /* 아이디 찾기 버튼 클릭 시 처리할 동작 */ }) {
                        Text(text = "회원탈퇴",
                            style = MaterialTheme.typography.body1.copy(color = Color.Red)
                        )
                    }

                }
            }

        }

    )

}