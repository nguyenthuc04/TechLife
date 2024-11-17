package com.snapco.techlife.ui.view.fragment.home

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.snapco.techlife.R
import com.snapco.techlife.data.model.home.post.Post
import com.snapco.techlife.databinding.FragmentCreatePostBinding
import com.snapco.techlife.ui.viewmodel.home.HomeViewModel
import java.util.UUID

class CreatePostFragment : Fragment() {
    private lateinit var binding: FragmentCreatePostBinding
    private var imageUri: Uri? = null
    private lateinit var imageBitmap: Bitmap
    private val homeViewModel: HomeViewModel by activityViewModels()

    companion object {
        const val REQUEST_IMAGE_PICK = 1
        const val REQUEST_IMAGE_CAPTURE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        binding.imageToPost.setOnClickListener { showImageSourceDialog() }
        binding.postBtn.setOnClickListener { handlePostCreation() }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        AlertDialog.Builder(requireContext()).setTitle("Select Image Source")
            .setItems(options) { dialog, which ->
                when (options[which]) {
                    "Take Photo" -> openCamera()
                    "Choose from Gallery" -> openGallery()
                    "Cancel" -> dialog.dismiss()
                }
            }.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val bitmap = data?.extras?.get("data") as? Bitmap
                    bitmap?.let {
                        imageUri = saveImageAndReturnUri(it)
                        displaySelectedImage(it)
                    }
                }

                REQUEST_IMAGE_PICK -> {
                    val uri = data?.data
                    uri?.let {
                        imageUri = it
                        val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, it)
                        displaySelectedImage(bitmap)
                    }
                }
            }
        }
    }

    private fun displaySelectedImage(bitmap: Bitmap) {
        imageBitmap = bitmap
        binding.imageToPost.setImageBitmap(bitmap)
        Toast.makeText(context, "Image selected successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun saveImageAndReturnUri(bitmap: Bitmap): Uri {
        val path =
            MediaStore.Images.Media.insertImage(context?.contentResolver, bitmap, "New Post", null)
        return Uri.parse(path)
    }

    private fun handlePostCreation() {
        val username = binding.username.text.toString().trim()
        val userImageUrl = binding.userImageUrl.text.toString().trim()
        val caption = binding.addCaption.text.toString().trim()

        if (username.isEmpty() || caption.isEmpty()) {
            Toast.makeText(context, "Please enter both username and caption.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        imageUri?.let {
            val newPost = Post(
                postId = UUID.randomUUID().toString(),
                caption = caption,
                imageUrl = it.toString(),
                userName = username,
                userId = UUID.randomUUID().toString(),
                createdAt = "Today",
                likesCount = 0,
                commentsCount = 0,
                userImageUrl = userImageUrl,
                isLiked = false
            )

            homeViewModel.createPost(newPost)
            Toast.makeText(context, "Post created successfully!", Toast.LENGTH_SHORT).show()
            resetFields()
        } ?: Toast.makeText(context, "Please select an image.", Toast.LENGTH_SHORT).show()
    }

    private fun resetFields() {
        binding.username.text?.clear()
        binding.userImageUrl.text?.clear()
        binding.addCaption.text?.clear()
        binding.imageToPost.setImageResource(R.drawable.image_placeholder)
        imageUri = null
    }
}
