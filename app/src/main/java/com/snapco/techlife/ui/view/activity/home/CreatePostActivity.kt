package com.snapco.techlife.ui.view.activity.home

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.R
import com.snapco.techlife.data.model.CreatePostRequest
import com.snapco.techlife.data.model.GetUserResponse
import com.snapco.techlife.databinding.ActivityCreatePostBinding
import com.snapco.techlife.extensions.CloudinaryUploader
import com.snapco.techlife.extensions.gone
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.extensions.visible
import com.snapco.techlife.ui.view.adapter.ImageAdapter
import com.snapco.techlife.ui.view.fragment.bottomsheet.BottomSheetCreatePostFragment
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel
import com.snapco.techlife.ui.viewmodel.objectdataholder.GetUserResponseHolder
import com.snapco.techlife.ui.viewmodel.objectdataholder.UserDataHolder
import java.io.File

class CreatePostActivity :
    AppCompatActivity(),
    BottomSheetCreatePostFragment.OnImageClickListener {
    private lateinit var binding: ActivityCreatePostBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var cloudinaryUploader: CloudinaryUploader
    private val bottomSheetFragment by lazy { BottomSheetCreatePostFragment() }
    private var isBottomSheetVisible = false
    private lateinit var adapter: ImageAdapter
    private val selectedImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupWindowInsets()
        setupToolbar()
        cloudinaryUploader = CloudinaryUploader(this)
        GetUserResponseHolder.getGetUserResponse()?.let { response ->
            updateUI(response)
        }
        showCreatePostFragment()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.imageView6.setOnClickListener {
            val intent = Intent(this, GalleryActivity::class.java)
            intent.putStringArrayListExtra("selected_images", ArrayList(selectedImages))
            startActivityForResult(intent, 1002)
        }
        // Khi ấn vào BottomNavigationView
        binding.view.setOnClickListener {
            hideBottomNavigationView() // Ẩn BottomNavigationView
            hideKeyboard() // Ẩn bàn phím nếu đang mở
            showCreatePostFragment() // Hiển thị BottomSheetCreatePostFragment
        }

        binding.editTextLongText.setOnClickListener {
            if (isBottomSheetVisible) {
                hideBottomSheet()
                showBottomNavigationView()
            } else {
                showKeyboard()
                adjustBottomNavigationForKeyboard()
            }
        }
        binding.btnDone.setOnClickListener {
            uploadSelectedImages()
        }

        observeCreatePostResponse()
    }

    private fun updateUI(response: GetUserResponse) =
        binding.apply {
            imgAvatar.loadImage(response.user.avatar)
            textView20.text = response.user.nickname
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

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            val newSelectedImages = data?.getStringArrayListExtra("selected_images") ?: emptyList()
            selectedImages.clear()
            selectedImages.addAll(newSelectedImages)
            adapter = ImageAdapter(selectedImages)
            binding.recyclerView.adapter = adapter
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun showCreatePostFragment() {
        if (!bottomSheetFragment.isAdded) {
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            isBottomSheetVisible = true
            hideBottomNavigationView()
        }
    }

    private fun hideBottomSheet() {
        if (bottomSheetFragment.isAdded) {
            bottomSheetFragment.dismiss()
            isBottomSheetVisible = false
        }
    }

    fun showBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun showKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.editTextLongText.requestFocus()
        inputMethodManager.showSoftInput(binding.editTextLongText, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.editTextLongText.windowToken, 0)
    }

    private fun adjustBottomNavigationForKeyboard() {
        binding.main.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            binding.main.getWindowVisibleDisplayFrame(rect)
            val screenHeight = binding.main.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Bàn phím đã mở
                binding.bottomNavigationView.translationY = -keypadHeight.toFloat()
            } else {
                // Bàn phím đã đóng
                binding.bottomNavigationView.translationY = 0f
            }
        }
    }

    private fun uploadSelectedImages() {
        binding.btnDone.text = ""
        binding.progressBar.visible()
        Log.d("SelectedImages", selectedImages.toString())
        val uploadedImageUrls = mutableListOf<String>()
        var uploadCount = 0
        selectedImages.forEach { imagePath ->
            val file = File(imagePath)
            val uri = Uri.fromFile(file)
            uploadImageToCloudinary(uri, uploadedImageUrls) {
                uploadCount++
                if (uploadCount == selectedImages.size) {
                    postMG(uploadedImageUrls)
                }
            }
        }
    }

    private fun uploadImageToCloudinary(
        uri: Uri,
        uploadedImageUrls: MutableList<String>,
        onUploadComplete: () -> Unit,
    ) {
        cloudinaryUploader.uploadMedia(
            uri,
            false,
            object : CloudinaryUploader.UploadCallback {
                override fun onProgress(progress: Int) {
                    // Handle progress
                }

                override fun onSuccess(url: String) {
                    uploadedImageUrls.add(url)
                    onUploadComplete()
                }

                override fun onError(errorMessage: String) {
                    // Handle error
                    onUploadComplete()
                }
            },
        )
    }

    private fun postMG(imageUrls: List<String>) {
        GetUserResponseHolder.getGetUserResponse()?.let { response ->
            UserDataHolder.getUserId()?.let {
                val createPostRequest =
                    CreatePostRequest(
                        userId = it,
                        caption = binding.editTextLongText.text.toString(),
                        imageUrl = imageUrls,
                        userName = response.user.nickname,
                        userImageUrl = response.user.avatar,
                    )

                Log.d("CreatePostRequest", createPostRequest.toString())
                homeViewModel.createPost(createPostRequest)
            }
        }
    }

    private fun observeCreatePostResponse() {
        homeViewModel.createPostResponse.observe(this) { response ->
            response?.let {
                if (response.success) {
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

    override fun onImageView6Click() {
        val intent = Intent(this, GalleryActivity::class.java)
        intent.putStringArrayListExtra("selected_images", ArrayList(selectedImages))
        startActivityForResult(intent, 1002)
    }

    override fun onImageView7Click() {
        val intent = Intent(this, GalleryActivity::class.java)
        intent.putStringArrayListExtra("selected_images", ArrayList(selectedImages))
        startActivityForResult(intent, 1002)
    }
}
