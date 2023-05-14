package com.slembers.alarmony.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

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
        outputStream.close()
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
    fun uriToMultiPart(uri: Uri, context: Context): MultipartBody.Part {
        val MAX_IMAGE_SIZE_BYTES = 1000000
        val bitmap = uri.let {
            if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images
                    .Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }
        }
        val resizedBitmap = compressBitmap(bitmap,MAX_IMAGE_SIZE_BYTES)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, resizedBitmap, "Title", null)
        val file = toFile(context, Uri.parse(path))
        return FormDataUtil.getImageMultipart("imgProfileFile", file)
    }

    private fun compressBitmap(bitmap: Bitmap, maxSize: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        Log.d("sizebefore","width $width height $height")
        var compressedBitmap = bitmap
        while (width * height > maxSize) {
            width /= 2
            height /= 2
            Log.d("sizeafter","width $width height $height")
            compressedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true)
        }
        return compressedBitmap
    }


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