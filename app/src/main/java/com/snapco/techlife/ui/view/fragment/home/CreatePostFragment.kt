package com.snapco.techlife.ui.view.fragment.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.snapco.techlife.R
import com.snapco.techlife.data.model.home.Post
import com.snapco.techlife.databinding.FragmentCreatePostBinding
import com.snapco.techlife.ui.viewmodel.home.SharedViewModel
import java.util.UUID

class CreatePostFragment : Fragment() {
    private lateinit var binding: FragmentCreatePostBinding
    private var uri: Uri? = null
    private lateinit var bitmap: Bitmap
    private var postId: String = UUID.randomUUID().toString()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    companion object {
        const val REQUEST_IMAGE_PICK = 1
        const val REQUEST_IMAGE_CAPTURE = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        // Set click listener for image selection
        binding.imageToPost.setOnClickListener {
            addPostDialog()
        }

        // Handle post button click
        binding.postBtn.setOnClickListener {
            if (uri != null) {
                postImage(uri)
            } else {
                Toast.makeText(context, "Please select an image to post.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addPostDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose your image source")
        builder.setItems(options) { dialog, item ->
            when (options[item]) {
                "Take Photo" -> takePhotoWithCamera()
                "Choose from Gallery" -> pickImageFromGallery()
                "Cancel" -> dialog.dismiss()
            }
        }
        builder.show()
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun pickImageFromGallery() {
        val pickPictureIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (pickPictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(pickPictureIntent, REQUEST_IMAGE_PICK)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhotoWithCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    uri = getImageUriFromBitmap(imageBitmap)
                    displaySelectedImage(imageBitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    val imageUri = data?.data
                    if (imageUri != null) {
                        uri = imageUri
                        val imageBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
                        displaySelectedImage(imageBitmap)
                    }
                }
            }
        }
    }

    private fun displaySelectedImage(imageBitmap: Bitmap) {
        bitmap = imageBitmap
        binding.imageToPost.setImageBitmap(imageBitmap)
        Toast.makeText(context, "Image selected successfully!", Toast.LENGTH_SHORT).show()
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val path = MediaStore.Images.Media.insertImage(context?.contentResolver, bitmap, "New Post", null)
        return Uri.parse(path)
    }

    private fun postImage(uri: Uri?) {
        val caption = binding.addCaption.text.toString()
        if (caption.isEmpty()) {
            Toast.makeText(context, "Please enter a caption.", Toast.LENGTH_SHORT).show()
            return
        }

        val newPost = Post(1,
            R.drawable.profile1.toString(), // Replace with user profile image or placeholder
            uri.toString(),
            "Hà Nội", // Replace with actual user name
            100, // Replace with actual location
            100,
            3,
            "Bùi Quang Vinh", // Initial like count
            R.drawable.profile1.toString(),
            isLiked = false,
            isOwnPost = false
        )

        sharedViewModel.setNewPost(newPost) // Add the new post to the ViewModel
        Toast.makeText(context, "Post created!", Toast.LENGTH_SHORT).show()
        clearFields()
    }

    private fun clearFields() {
        binding.addCaption.text.clear()
        binding.imageToPost.setImageResource(R.drawable.image_placeholder) // Reset to placeholder
        uri = null
    }
}
