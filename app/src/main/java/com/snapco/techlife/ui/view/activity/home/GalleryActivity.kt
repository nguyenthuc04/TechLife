package com.snapco.techlife.ui.view.activity.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.snapco.techlife.databinding.ActivityGalleryBinding
import com.snapco.techlife.ui.view.adapter.GalleryPostAdapter

class GalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var adapter: GalleryPostAdapter
    private val images = mutableListOf<String>()
    private val selectedImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val initialSelectedImages = intent.getStringArrayListExtra("selected_images")
        if (initialSelectedImages != null) {
            selectedImages.addAll(initialSelectedImages)
        }

        setupViews()
        checkPermissionAndLoadImages()
    }

    private fun setupViews() {
        // Setup RecyclerView
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        adapter = GalleryPostAdapter(images, selectedImages)
        binding.recyclerView.adapter = adapter

        // Setup toolbar buttons
        binding.btnClose.setOnClickListener {
            finish()
        }

        binding.btnDone.setOnClickListener {
            val selectedImages = adapter.getSelectedImages()
            if (selectedImages.isEmpty()) {
                finish()
            } else {
                // Handle selected images
                setResult(
                    RESULT_OK,
                    Intent().apply {
                        putStringArrayListExtra("selected_images", ArrayList(selectedImages))
                    },
                )
                finish()
            }
        }
    }

    private fun checkPermissionAndLoadImages() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES), 1001)
                return
            }
        } else {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1001)
                return
            }
        }
        loadImages()
    }

    private fun loadImages() {
        val projection =
            arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_TAKEN,
            )

        val cursor =
            contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                "${MediaStore.Images.Media.DATE_TAKEN} DESC",
            )

        cursor?.use {
            val pathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val path = it.getString(pathColumn)
                images.add(path)
            }
        }

        adapter.notifyDataSetChanged()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadImages()
        }
    }
}
