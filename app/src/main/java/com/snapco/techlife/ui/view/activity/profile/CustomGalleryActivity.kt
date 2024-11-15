package com.snapco.techlife.ui.view.activity.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.data.model.UpdateUserRequest
import com.snapco.techlife.databinding.ActivityCustomGalleryBinding
import com.snapco.techlife.databinding.ItemGalleryImageBinding
import com.snapco.techlife.extensions.CloudinaryUploader
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.showToast
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.viewmodel.UserViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import java.io.ByteArrayOutputStream
import java.io.File

class CustomGalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomGalleryBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var cloudinaryUploader: CloudinaryUploader
    private val imageUris = mutableListOf<Uri>()
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cloudinaryUploader = CloudinaryUploader(this) // Initialize cloudinaryUploader
        checkAndRequestPermissions()
        setupToolbar()
        setupRecyclerView()
        observeUpdateUserResponse()
    }

    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_CODE)
            } else {
                loadImagesFromGallery()
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_CODE)
            } else {
                loadImagesFromGallery()
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            findViewById<TextView>(R.id.tvCancel).setOnClickListener { finish() }
            findViewById<TextView>(R.id.tvDone).setOnClickListener { onDone() }
        }
    }

    private fun setupRecyclerView() {
        binding.imageView.post {
            binding.imageView.setScale(1.5f, true)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 4)
        binding.recyclerView.adapter = GalleryAdapter(imageUris) { uri -> onImageSelected(uri) }
    }

    private fun loadImagesFromGallery() {
        Log.d("CustomGalleryActivity", "Loading images from gallery")
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA)
        val cursor =
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC",
            )

        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val imagePath = it.getString(columnIndex)
                val imageUri = Uri.fromFile(File(imagePath))
                imageUris.add(imageUri)
                Log.d("CustomGalleryActivity", "Image path: $imagePath")
            }
            binding.recyclerView.adapter?.notifyDataSetChanged()
            if (imageUris.isNotEmpty()) {
                Log.d("CustomGalleryActivity", "First image URI: ${imageUris[0]}")
                onImageSelected(imageUris[0])
            } else {
                Log.d("CustomGalleryActivity", "No images found")
            }
        } ?: Log.d("CustomGalleryActivity", "Cursor is null")
    }

    private fun onImageSelected(uri: Uri) {
        Log.d("CustomGalleryActivity", "Selected image URI: $uri")
        selectedImageUri = uri
        binding.imageView.setImageURI(uri)
        binding.imageView.post {
            binding.imageView.setScale(1.5f, true) // Phóng to 50%
        }
    }

    private fun getBitmapFromImageView(imageView: ImageView): Bitmap {
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(imageView.drawingCache)
        imageView.isDrawingCacheEnabled = false
        return bitmap
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

    private fun onDone() =
        binding.apply {
            selectedImageUri?.let {
                val bitmap = getBitmapFromImageView(binding.imageView)
                val uri = getImageUriFromBitmap(this@CustomGalleryActivity, bitmap)
                progressBar.visible()
                tvDone.gone()
                cloudinaryUploader.uploadMedia(
                    uri,
                    false,
                    object : CloudinaryUploader.UploadCallback {
                        override fun onProgress(progress: Int) {
                            progressBar.progress = progress
                        }

                        override fun onSuccess(url: String) {
                            progressBar.gone()
                            uploadedImage(url)
                        }

                        override fun onError(errorMessage: String) {
                            progressBar.gone()
                            tvDone.visible()
                        }
                    },
                )
            } ?: run {
                showToast("Vui lòng chọn một hình ảnh")
            }
        }

    private fun uploadedImage(url: String) {
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
            GetUserResponseHolder.setGetUserResponse(
                response.copy(user = response.user.copy(avatar = url)),
            )
            userViewModel.updateUser(userId, updateUserRequest)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadImagesFromGallery()
        } else {
            Log.d("CustomGalleryActivity", "Permission denied")
        }
    }

    companion object {
        private const val REQUEST_CODE = 100
    }
}

class GalleryAdapter(
    private val imageUris: List<Uri>,
    private val onImageSelected: (Uri) -> Unit,
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {
    inner class ViewHolder(
        private val binding: ItemGalleryImageBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            binding.imageView.setImageURI(uri)
            binding.imageView.setOnClickListener { onImageSelected(uri) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = ItemGalleryImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(imageUris[position])
    }

    override fun getItemCount(): Int = imageUris.size
}
