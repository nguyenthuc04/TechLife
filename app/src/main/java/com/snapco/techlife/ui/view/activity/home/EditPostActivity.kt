package com.snapco.techlife.ui.view.activity.home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.snapco.techlife.R
import com.snapco.techlife.databinding.ActivityEditPostBinding
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snapco.techlife.extensions.loadImage
import com.snapco.techlife.ui.view.adapter.ImageAdapter

class EditPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditPostBinding
    private lateinit var imageAdapter: ImageAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_post)

        // Retrieve post data from intent
        val postId = intent.getStringExtra("POST_ID")
        val userName = intent.getStringExtra("USER_NAME")
        val caption = intent.getStringExtra("CAPTION")
        val userImageUrl = intent.getStringExtra("USER_IMAGE_URL")
        val images = intent.getStringArrayListExtra("IMAGES")

        if (postId == null) {
            Toast.makeText(this, "Error: Missing Post ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Pre-fill fields with post data
        binding.userName.text = userName
        binding.userAvatar.loadImage(userImageUrl)
        binding.editCaption.setText(caption)

        setupImageRecyclerView(images)

        // Set up Save button
        binding.btnSave.setOnClickListener {
            val updatedCaption = binding.editCaption.text.toString().trim()

            if (updatedCaption.isEmpty()) {
                Toast.makeText(this, "Caption cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call ViewModel to update the post
            homeViewModel.updatePost(postId, updatedCaption)
            Toast.makeText(this, "Post updated successfully", Toast.LENGTH_SHORT).show()

            // Close the activity
            finish()
        }

        // Set up Cancel button
        binding.btnCancel.setOnClickListener {
            finish() // Close the activity without saving
        }

        // Observe update success from ViewModel
        observeUpdateResult()
    }

    private fun observeUpdateResult() {
        homeViewModel.updateResult.observe(this) { result ->
            if (result.isSuccess) {
                Toast.makeText(this, "Post updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update post. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupImageRecyclerView(images: List<String>?) {
        imageAdapter = ImageAdapter(images ?: listOf())
        binding.postImage.apply {
            layoutManager = LinearLayoutManager(this@EditPostActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = imageAdapter
        }
    }
}
