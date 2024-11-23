package com.snapco.techlife.ui.view.activity.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
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
import com.snapco.techlife.data.model.CreateReelRequest
import com.snapco.techlife.data.model.GetUserResponse
import com.snapco.techlife.databinding.ActivityCreateReelBinding
import com.snapco.techlife.extensions.CloudinaryUploader
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.viewmodel.ReelViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder

class CreateReelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateReelBinding
    private val reelViewModel: ReelViewModel by viewModels()
    private lateinit var cloudinaryUploader: CloudinaryUploader
    private var selectedVideoUri: Uri? = null
    private val PICK_VIDEO_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateReelBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        setupToolbar()
        GetUserResponseHolder.getGetUserResponse()?.let { response ->
            updateUI(response)
        }
        cloudinaryUploader = CloudinaryUploader(this)
        binding.buttonChooseVideo.setOnClickListener {
            checkPermissionAndPickVideo()
        }
        binding.btnDone.setOnClickListener {
            uploadVideo()
        }
        observeCreateReelResponse()
    }

    private fun updateUI(response: GetUserResponse) =
        binding.apply {
            imgAvatar.loadImage(response.user.avatar)
            textView20.text = response.user.nickname
        }

    private fun checkPermissionAndPickVideo() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                checkAndRequestPermission(Manifest.permission.READ_MEDIA_VIDEO)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                checkAndRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            else -> {
                pickVideo()
            }
        }
    }

    private fun checkAndRequestPermission(permission: String) {
        when {
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
                pickVideo()
            }
            shouldShowRequestPermissionRationale(permission) -> {
                showPermissionRationale(permission)
            }
            else -> {
                requestPermission(permission)
            }
        }
    }

    private fun showPermissionRationale(permission: String) {
        Toast.makeText(this, "Ứng dụng cần quyền truy cập video để chọn và tải lên", Toast.LENGTH_LONG).show()
        requestPermission(permission)
    }

    private fun requestPermission(permission: String) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
    }

    private fun pickVideo() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_VIDEO_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickVideo()
                } else {
                    Toast.makeText(this, "Quyền truy cập bị từ chối. Không thể chọn video.", Toast.LENGTH_LONG).show()
                    showManualPermissionInstructions()
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedVideoUri = data.data
            showSelectedVideo()
        }
    }

    private fun uploadVideo() {
        selectedVideoUri?.let { uri ->
            binding.progressBar.visibility = View.VISIBLE

            cloudinaryUploader.uploadMedia(
                uri,
                true,
                object : CloudinaryUploader.UploadCallback {
                    override fun onProgress(progress: Int) {
                        binding.progressBar.progress = progress
                    }

                    override fun onSuccess(url: String) {
                        posReel(listOf(url))
                    }

                    override fun onError(errorMessage: String) {
                        binding.progressBar.visibility = View.GONE
                    }
                },
            )
        } ?: run {
            Toast.makeText(this, "Vui lòng chọn một video", Toast.LENGTH_SHORT).show()
        }
    }

    private fun posReel(videoUrl: List<String>) {
        GetUserResponseHolder.getGetUserResponse()?.let { response ->
            UserDataHolder.getUserId()?.let {
                val createPostRequest =
                    CreateReelRequest(
                        userId = it,
                        caption = binding.editTextLongText.text.toString(),
                        videoUrl = videoUrl,
                        userName = response.user.nickname,
                        userImageUrl = response.user.avatar,
                    )

                Log.d("CreatePostRequest", createPostRequest.toString())
                reelViewModel.createReel(createPostRequest)
            }
        }
    }

    private fun observeCreateReelResponse() {
        reelViewModel.createReelResponse.observe(this) { response ->
            response?.let {
                if (response.message == "Post created successfully") {
                    binding.btnDone.text = "Đăng"
                    binding.progressBar.gone()
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
            .setMessage("tạo bài thất bại. Vui lòng thử lại.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showSelectedVideo() {
        selectedVideoUri?.let { uri ->
            binding.videoViewSelected.setVideoURI(uri)
            binding.videoViewSelected.visibility = View.VISIBLE
            binding.videoViewSelected.start()
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar5)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(!intent.getBooleanExtra("hideBackButton", false))
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new)
        }
    }

    private fun showManualPermissionInstructions() {
        val message =
            "Để cấp quyền thủ công, vui lòng:\n" +
                "1. Mở Cài đặt điện thoại\n" +
                "2. Tìm và chọn ứng dụng này\n" +
                "3. Chọn Quyền\n" +
                "4. Cấp quyền truy cập Video"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
