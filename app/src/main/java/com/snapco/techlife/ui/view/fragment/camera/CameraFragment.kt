package com.snapco.techlife.ui.view.fragment.camera

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.snapco.techlife.R
import com.snapco.techlife.ui.viewmodel.CameraViewModel
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class CameraFragment : Fragment() {

    private lateinit var cameraView: CameraView
    private lateinit var captureButton: Button
    private lateinit var recordButton: Button
    private lateinit var switchCameraButton: Button
    private val cameraViewModel: CameraViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        cameraView = view.findViewById(R.id.cameraView)
        captureButton = view.findViewById(R.id.btnCapture)
        recordButton = view.findViewById(R.id.btnrecord)
        switchCameraButton = view.findViewById(R.id.btnSwitchCamera)

        // Thiết lập CameraView
        cameraView.setLifecycleOwner(viewLifecycleOwner)
        cameraView.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                savePicture(result)
            }

            override fun onVideoTaken(result: VideoResult) {
                super.onVideoTaken(result)
                saveVideo(result)
            }
        })

        // Xử lý sự kiện nhấn nút chụp ảnh
        captureButton.setOnClickListener {
            cameraView.takePicture()
        }

        // Xử lý sự kiện nhấn nút ghi video
        recordButton.setOnClickListener {
            if (cameraViewModel.isRecording.value == true) {
                cameraView.stopVideo()
                cameraViewModel.stopRecording()
            } else {
                cameraView.takeVideoSnapshot(getTemporaryFile(requireContext(), "video"))
                cameraViewModel.startRecording()
            }
        }

        // Xử lý sự kiện nhấn nút chuyển đổi camera
        switchCameraButton.setOnClickListener {
            cameraViewModel.switchCamera()
        }

        // Quan sát thay đổi từ ViewModel
        cameraViewModel.facing.observe(viewLifecycleOwner, Observer { facing ->
            cameraView.facing = facing
        })

        cameraViewModel.isRecording.observe(viewLifecycleOwner, Observer { isRecording ->
            recordButton.text = if (isRecording) "Stop" else "Record"
        })

        return view
    }

    private fun getTemporaryFile(context: Context, type: String): File {
        val storageDir: File? = context.getExternalFilesDir(
            if (type == "video") Environment.DIRECTORY_MOVIES else Environment.DIRECTORY_PICTURES)
        return File.createTempFile("TEMP_", if (type == "video") ".mp4" else ".jpg", storageDir)
    }

    private fun savePicture(result: PictureResult) {
        result.toFile(getTemporaryFile(requireContext(), "picture")) { file ->
            if (file != null) {
                saveToGallery(file, "image/jpeg", Environment.DIRECTORY_PICTURES)
            } else {
                Toast.makeText(context, "Failed to save picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveVideo(result: VideoResult) {
        val file = result.file
        if (file != null) {
            saveToGallery(file, "video/mp4", Environment.DIRECTORY_MOVIES)
        } else {
            Toast.makeText(context, "Failed to save video", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToGallery(file: File, mimeType: String, directory: String) {
        val resolver = requireContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
        }

        val uri = resolver.insert(
            if (mimeType.startsWith("image")) MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            else MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            contentValues)

        if (uri != null) {
            try {
                val outputStream = resolver.openOutputStream(uri)
                outputStream?.use { stream ->
                    FileInputStream(file).use { inputStream ->
                        inputStream.copyTo(stream)
                    }
                }
                Toast.makeText(context, if (mimeType.startsWith("image")) "Picture saved to gallery" else "Video saved to gallery", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                Toast.makeText(context, "Failed to save ${if (mimeType.startsWith("image")) "picture" else "video"}: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                file.delete()
            }
        } else {
            Toast.makeText(context, "Failed to create new MediaStore record", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraView.destroy()
    }
}
