package com.snapco.techlife.ui.view.activity.premium

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityPayPremiumBinding
import com.snapco.techlife.extensions.CloudinaryUploader

class PayPremiumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayPremiumBinding

    private lateinit var cloudinaryUploader: CloudinaryUploader
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Kích hoạt hiển thị toàn màn hình
        enableEdgeToEdge()
        // Khởi tạo layout bằng ViewBinding
        binding = ActivityPayPremiumBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Áp dụng window insets cho view chính
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        cloudinaryUploader = CloudinaryUploader(this)

        binding.imgBill.setOnClickListener {
            checkPermissionAndPickImage()
        }

        binding.btnConfirm.setOnClickListener {
            uploadImage()
        }

    }

    private fun checkPermissionAndPickImage() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                checkAndRequestPermission(Manifest.permission.READ_MEDIA_IMAGES)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                checkAndRequestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            else -> {
                pickImage()
            }
        }
    }

    private fun checkAndRequestPermission(permission: String) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                pickImage()
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
        Toast.makeText(
            this,
            "Ứng dụng cần quyền truy cập hình ảnh để chọn và tải lên",
            Toast.LENGTH_LONG
        ).show()
        requestPermission(permission)
    }

    private fun requestPermission(permission: String) {
        ActivityCompat.requestPermissions(this, arrayOf(permission), PERMISSION_REQUEST_CODE)
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage()
                } else {
                    Toast.makeText(
                        this,
                        "Quyền truy cập bị từ chối. Không thể chọn hình ảnh.",
                        Toast.LENGTH_LONG
                    ).show()
                    showManualPermissionInstructions()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            showSelectedImage()
        }
    }

    private fun showSelectedImage() {
        selectedImageUri?.let { uri ->
            Glide.with(this).load(uri).into(binding.imgBill)
            binding.imgBill.visibility = View.VISIBLE
        }
    }

    private fun uploadImage() {
        selectedImageUri?.let { uri ->
            binding.progressBar.visibility = View.VISIBLE
            binding.textViewStatus.text = "Đang tải lên..."
            cloudinaryUploader.uploadMedia(uri, false, object : CloudinaryUploader.UploadCallback {
                override fun onProgress(progress: Int) {
                    binding.progressBar.progress = progress
                }

                override fun onSuccess(url: String) {
                    binding.progressBar.visibility = View.GONE
                    binding.textViewStatus.text = "Tải lên thành công"
                    showUploadedImage(url)
                }

                override fun onError(errorMessage: String) {
                    binding.progressBar.visibility = View.GONE
                    binding.textViewStatus.text = "Lỗi: $errorMessage"
                }
            })
        } ?: run {
            Toast.makeText(this, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showUploadedImage(url: String) {
        Glide.with(this).load(url).into(binding.imgBill)
        binding.imgBill.visibility = View.VISIBLE
    }

    private fun showManualPermissionInstructions() {
        val message = "Để cấp quyền thủ công, vui lòng:\n" +
                "1. Mở Cài đặt điện thoại\n" +
                "2. Tìm và chọn ứng dụng này\n" +
                "3. Chọn Quyền\n" +
                "4. Cấp quyền truy cập Hình ảnh"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
