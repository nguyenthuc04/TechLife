package com.snapco.techlife.ui.view.activity.course

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.snapco.techlife.R
import com.snapco.techlife.data.model.CreateCourseRequest
import com.snapco.techlife.databinding.ActivityAddCourseBinding
import com.snapco.techlife.extensions.CloudinaryUploader
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.viewmodel.CourseViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import java.util.*

class AddCourse : AppCompatActivity() {
    private val courseViewModel: CourseViewModel by viewModels()
    private lateinit var binding: ActivityAddCourseBinding
    private lateinit var cloudinaryUploader: CloudinaryUploader
    private var selectedImageUri: Uri? = null
    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 2
    private var startTime: String? = null
    private var endTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCourseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToolbar()
        cloudinaryUploader = CloudinaryUploader(this)
        binding.imgAvatar.loadImage(UserDataHolder.getUserAvatar())
        binding.textView20.text = UserDataHolder.getUserName()
        binding.imageView11.setOnClickListener {
            checkPermissionAndPickImage()
        }
        binding.btnDone.setOnClickListener {
            uploadImage()
        }
        binding.editTextText2.setOnClickListener {
            showDatePickerDialog()
        }
        val typeKH = listOf("Cơ bản", "Nâng cao", "Chuyên môn hoá")

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, // Layout mặc định cho item
            typeKH
        )


        // Thiết lập giao diện dropdown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnType.adapter = adapter
        observeCreatePremiumResponse()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, startYear, startMonth, startDay ->
            startTime = String.format("%02d-%02d-%d", startDay, startMonth + 1, startYear)
            DatePickerDialog(this, { _, endYear, endMonth, endDay ->
                endTime = String.format("%02d-%02d-%d", endDay, endMonth + 1, endYear)
                binding.editTextText2.setText("Start: $startTime, End: $endTime")
            }, year, month, day).show()
        }, year, month, day).show()
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
                permission,
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
        Toast
            .makeText(
                this,
                "Ứng dụng cần quyền truy cập hình ảnh để chọn và tải lên",
                Toast.LENGTH_LONG,
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
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImage()
                } else {
                    Toast
                        .makeText(
                            this,
                            "Quyền truy cập bị từ chối. Không thể chọn hình ảnh.",
                            Toast.LENGTH_LONG,
                        ).show()
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data
            showSelectedImage()
        }
    }

    private fun showSelectedImage() {
        selectedImageUri?.let { uri ->
            Glide.with(this).load(uri).into(binding.imageView11)
        }
    }

    private fun uploadImage() {
        selectedImageUri?.let { uri ->
            binding.progressBar.visibility = View.VISIBLE
            cloudinaryUploader.uploadMedia(
                uri,
                false,
                object : CloudinaryUploader.UploadCallback {
                    override fun onProgress(progress: Int) {
                        binding.progressBar.progress = progress
                    }

                    override fun onSuccess(url: String) {
                        putCourse(url)
                    }

                    override fun onError(errorMessage: String) {
                        binding.progressBar.visibility = View.GONE
                    }
                },
            )
        } ?: run {
            Toast.makeText(this, "Vui lòng chọn một hình ảnh", Toast.LENGTH_SHORT).show()
        }
    }

    private fun putCourse(url: String) {
        GetUserResponseHolder.getGetUserResponse()?.let { response ->
            UserDataHolder.getUserId()?.let {
                val createCourseRequest =
                    CreateCourseRequest(
                        userId = it,
                        userName = response.user.nickname,
                        userImageUrl = response.user.avatar,
                        imageUrl = url,
                        name = binding.editTextText3.text.toString(),
                        quantity = binding.editTextText4.text.toString(),
                        price = binding.editTextText7.text.toString(),
                        duration = binding.editTextText5.text.toString(),
                        describe = binding.editTextText6.text.toString(),
                        startDate = startTime!!,
                        endDate = endTime!!,
                        type = binding.spnType.selectedItem.toString(),
                    )
                courseViewModel.createCourse(createCourseRequest)
            }
        }
    }

    private fun observeCreatePremiumResponse() {
        courseViewModel.createCourseResponse.observe(this) { response ->
            response?.let {
                if (response.success) {
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
            .setMessage("Gửi thất bại. Vui lòng thử lại.")
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showManualPermissionInstructions() {
        val message =
            "Để cấp quyền thủ công, vui lòng:\n" +
                "1. Mở Cài đặt điện thoại\n" +
                "2. Tìm và chọn ứng dụng này\n" +
                "3. Chọn Quyền\n" +
                "4. Cấp quyền truy cập Hình ảnh"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar5)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(!intent.getBooleanExtra("hideBackButton", false))
            setDisplayShowTitleEnabled(false)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new)
        }
    }
}
