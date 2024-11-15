package com.snapco.techlife.extensions

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CloudinaryUploader(
    private val context: Context,
) {
    fun uploadMedia(
        uri: Uri,
        isVideo: Boolean,
        callback: UploadCallback,
    ) {
        val tempFile = createTempFileFromUri(uri, isVideo)
        if (tempFile != null) {
            val resourceType = if (isVideo) "video" else "image"
            MediaManager
                .get()
                .upload(tempFile.absolutePath)
                .option("resource_type", resourceType)
                .option("type", "upload")
                .callback(
                    object : com.cloudinary.android.callback.UploadCallback {
                        override fun onStart(requestId: String) {
                            // Bắt đầu tải lên
                        }

                        override fun onProgress(
                            requestId: String,
                            bytes: Long,
                            totalBytes: Long,
                        ) {
                            val progress = (bytes.toDouble() / totalBytes * 100).toInt()
                            callback.onProgress(progress)
                        }

                        override fun onSuccess(
                            requestId: String,
                            resultData: Map<*, *>,
                        ) {
                            val url = resultData["url"] as String
                            callback.onSuccess(url)
                            tempFile.delete() // Xóa file tạm sau khi tải lên thành công
                        }

                        override fun onError(
                            requestId: String,
                            error: ErrorInfo,
                        ) {
                            callback.onError(error.description)
                            tempFile.delete() // Xóa file tạm nếu có lỗi
                        }

                        override fun onReschedule(
                            requestId: String,
                            error: ErrorInfo,
                        ) {
                            // Xử lý lập lịch lại
                        }
                    },
                ).dispatch()
        } else {
            callback.onError("Không thể tạo file tạm thời")
        }
    }

    private fun createTempFileFromUri(
        uri: Uri,
        isVideo: Boolean,
    ): File? {
        try {
            val stream = context.contentResolver.openInputStream(uri)
            if (stream != null) {
                val extension =
                    MimeTypeMap
                        .getSingleton()
                        .getExtensionFromMimeType(context.contentResolver.getType(uri))
                val prefix = if (isVideo) "temp_video" else "temp_image"
                val file = File.createTempFile(prefix, ".$extension", context.cacheDir)
                FileOutputStream(file).use { output ->
                    stream.copyTo(output)
                }
                return file
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    interface UploadCallback {
        fun onProgress(progress: Int)

        fun onSuccess(url: String)

        fun onError(errorMessage: String)
    }
}
