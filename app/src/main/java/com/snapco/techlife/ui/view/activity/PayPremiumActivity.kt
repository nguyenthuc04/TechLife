package com.snapco.techlife.ui.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityPayPremiumBinding

class PayPremiumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayPremiumBinding
    private val IMAGE_REQUEST = 2000
    private var imageUri: Uri? = null

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

        // Thiết lập sự kiện click cho imgBill để mở thư viện ảnh
        binding.imgBill.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Yêu cầu quyền truy cập nếu chưa được cấp
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    IMAGE_REQUEST
                )
            } else {
                // Mở thư viện ảnh nếu quyền đã được cấp
                openGallery()
            }
        }
    }

    // Hàm mở thư viện ảnh
    private fun openGallery() {
        val photoIntent = Intent(Intent.ACTION_PICK)
        photoIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        startActivityForResult(photoIntent, IMAGE_REQUEST)
    }

    // Xử lý kết quả trả về từ thư viện ảnh
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                IMAGE_REQUEST -> {
                    // Đặt URI của ảnh đã chọn vào imgBill
                    imageUri = data?.data
                    binding.imgBill.setImageURI(imageUri)
                }
            }
        }
    }

    // Xử lý kết quả của yêu cầu quyền truy cập
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == IMAGE_REQUEST && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Mở thư viện ảnh nếu quyền đã được cấp
            openGallery()
        } else {
            // Hiển thị thông báo nếu quyền bị từ chối
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
}