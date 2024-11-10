package com.snapco.techlife.ui.view.activity.profile

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityCustomGalleryBinding
import com.snapco.techlife.databinding.ItemGalleryImageBinding
import com.snapco.techlife.ui.viewmodel.UserViewModel
import java.io.File

class CustomGalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomGalleryBinding
    private val userViewModel: UserViewModel by viewModels()
    private val imageUris = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAndRequestPermissions()
        setupToolbar()
        setupRecyclerView()
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
        binding.imageView.setImageURI(uri)
    }

    private fun onDone() {
        // Handle the done action, e.g., return the selected image URI to the calling activity
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
