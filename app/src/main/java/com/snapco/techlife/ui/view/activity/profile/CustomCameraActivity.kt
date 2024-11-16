package com.snapco.techlife.ui.view.activity.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.data.model.UpdateUserRequest
import com.snapco.techlife.databinding.ActivityCustomCameraBinding
import com.snapco.techlife.extensions.CloudinaryUploader
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import java.io.ByteArrayOutputStream

class CustomCameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomCameraBinding
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var cameraDevice: CameraDevice
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    private lateinit var cloudinaryUploader: CloudinaryUploader
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var imageReader: ImageReader

    private var cameraId = "0" // Mặc định là camera sau
    private var flashMode = CaptureRequest.FLASH_MODE_OFF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCustomCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cloudinaryUploader = CloudinaryUploader(this)
        setupToolbar()
        setupWindowInsets()
        setUpOnClick()
        if (allPermissionsGranted()) {
            binding.textureView.surfaceTextureListener = surfaceTextureListener
        } else {
            requestPermissions()
        }
        observeUpdateUserResponse()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            findViewById<TextView>(R.id.tvCancel1).setOnClickListener { finish() }
        }
    }

    private fun setUpOnClick() =
        binding.apply {
            cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager

            switchCameraButton.setOnClickListener { switchCamera() }
            captureButton.setOnClickListener { captureImage() }
            flashButton.setOnClickListener { toggleFlash() }
        }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun allPermissionsGranted() =
        REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    private val surfaceTextureListener =
        object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(
                texture: SurfaceTexture,
                width: Int,
                height: Int,
            ) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(
                texture: SurfaceTexture,
                width: Int,
                height: Int,
            ) {}

            override fun onSurfaceTextureDestroyed(texture: SurfaceTexture) = true

            override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {}
        }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        cameraManager.openCamera(cameraId, stateCallback, null)
    }

    private val stateCallback =
        object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                createCameraPreview()
            }

            override fun onDisconnected(camera: CameraDevice) {
                cameraDevice.close()
            }

            override fun onError(
                camera: CameraDevice,
                error: Int,
            ) {
                cameraDevice.close()
            }
        }

    private fun createCameraPreview() {
        val texture = binding.textureView.surfaceTexture
        texture?.setDefaultBufferSize(binding.textureView.width, binding.textureView.height)

        val surface = Surface(texture)
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        captureRequestBuilder.addTarget(surface)

        // Thiết lập ImageReader với kích thước khớp với TextureView
        imageReader = ImageReader.newInstance(binding.textureView.width, binding.textureView.height, ImageFormat.JPEG, 1)
        imageReader.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage()
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.capacity())
            buffer.get(bytes)
            image.close()

            // Convert bytes thành Bitmap
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val uri = getImageUriFromBitmap(this, bitmap)
            uploadImage(uri)
        }, null)

        // Thêm ImageReader surface
        cameraDevice.createCaptureSession(
            listOf(surface, imageReader.surface),
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    cameraCaptureSession = session
                    updatePreview()
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {}
            },
            null,
        )
    }

    private fun updatePreview() {
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        captureRequestBuilder.set(CaptureRequest.FLASH_MODE, flashMode)
        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, null)
    }

    private fun switchCamera() {
        cameraId = if (cameraId == "0") "1" else "0"
        cameraDevice.close()
        openCamera()
    }

    private fun captureImage() {
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        captureRequestBuilder.addTarget(imageReader.surface)
        captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, getJpegOrientation())
        cameraCaptureSession.capture(captureRequestBuilder.build(), null, null)
    }

    private fun getJpegOrientation(): Int {
        val deviceRotation = windowManager.defaultDisplay.rotation
        val sensorOrientation = cameraManager.getCameraCharacteristics(cameraId).get(CameraCharacteristics.SENSOR_ORIENTATION)!!
        val deviceOrientation = ORIENTATIONS[deviceRotation]
        return (sensorOrientation + deviceOrientation + 360) % 360
    }

    private val ORIENTATIONS =
        SparseIntArray().apply {
            append(Surface.ROTATION_0, 0)
            append(Surface.ROTATION_90, 90)
            append(Surface.ROTATION_180, 180)
            append(Surface.ROTATION_270, 270)
        }

    override fun onPause() {
        super.onPause()
        cameraDevice.close()
    }

    private fun getImageUriFromBitmap(
        context: Context,
        bitmap: Bitmap,
    ): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    private fun uploadImage(uri: Uri) {
        cloudinaryUploader.uploadMedia(
            uri,
            false,
            object : CloudinaryUploader.UploadCallback {
                override fun onProgress(progress: Int) {}

                override fun onSuccess(url: String) {
                    updateUserProfile(url)
                }

                override fun onError(errorMessage: String) {
                    Toast.makeText(this@CustomCameraActivity, "Upload failed: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            },
        )
    }

    private fun updateUserProfile(url: String) {
        val userId = UserDataHolder.getUserId() ?: return
        GetUserResponseHolder.getGetUserResponse()?.let { response ->
            val updateUserRequest =
                UpdateUserRequest(
                    account = response.user.account,
                    password = response.user.password,
                    birthday = response.user.birthday,
                    name = response.user.name,
                    nickname = response.user.nickname,
                    bio = response.user.bio,
                    avatar = url,
                    accountType = response.user.accountType,
                )
            GetUserResponseHolder.setGetUserResponse(response.copy(user = response.user.copy(avatar = url)))
            userViewModel.updateUser(userId, updateUserRequest)
        }
    }

    private fun toggleFlash() {
        flashMode =
            when (flashMode) {
                CaptureRequest.FLASH_MODE_OFF -> CaptureRequest.FLASH_MODE_TORCH
                CaptureRequest.FLASH_MODE_TORCH -> CaptureRequest.FLASH_MODE_OFF
                else -> CaptureRequest.FLASH_MODE_OFF
            }
        updatePreview()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                binding.textureView.surfaceTextureListener = surfaceTextureListener
            } else {
                finish()
            }
        }
    }

    private fun observeUpdateUserResponse() {
        userViewModel.updateUserResponse.observe(this) { response ->
            response?.let {
                if (response.message == "Người dùng đã được cập nhật thành công!") {
                    finish()
                } else {
                    showUpdateErrorDialog()
                }
            }
        }
    }

    private fun showUpdateErrorDialog() {
        AlertDialog
            .Builder(this)
            .setTitle("Lỗi")
            .setMessage("Cập nhật thất bại. Vui lòng thử lại.")
            .setPositiveButton("OK", null)
            .show()
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arrayOf(Manifest.permission.CAMERA)
            } else {
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
