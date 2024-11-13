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
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.snapco.techlife.databinding.FragmentCameraBinding
import com.snapco.techlife.ui.viewmodel.CameraViewModel
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class CameraFragment : Fragment() {

    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val cameraViewModel: CameraViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val view = binding.root


        binding.cameraView.setLifecycleOwner(viewLifecycleOwner)
        binding.cameraView.addCameraListener(object : CameraListener() {
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
        binding.btnCapture.setOnClickListener {
            binding.cameraView.takePicture()
        }

        // Xử lý sự kiện nhấn nút ghi video
        binding.btnrecord.setOnClickListener {
            if (cameraViewModel.isRecording.value == true) {
                binding.cameraView.stopVideo()
                cameraViewModel.stopRecording()
            } else {
                binding.cameraView.takeVideoSnapshot(getTemporaryFile(requireContext(), "video"))
                cameraViewModel.startRecording()
            }
        }

        // Xử lý sự kiện nhấn nút chuyển đổi camera
        binding.btnSwitchCamera.setOnClickListener {
            cameraViewModel.switchCamera()
        }

        // Quan sát thay đổi từ ViewModel
        cameraViewModel.facing.observe(viewLifecycleOwner, Observer { facing ->
            binding.cameraView.facing = facing
        })
        cameraViewModel.timerText.observe(viewLifecycleOwner, Observer { time ->
            binding.txtTimeRecord.text = time
        })

        cameraViewModel.isRecording.observe(viewLifecycleOwner, Observer { isRecording ->
            binding.btnrecord.text = if (isRecording) "Stop" else "Record"
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
        _binding = null
    }
}
