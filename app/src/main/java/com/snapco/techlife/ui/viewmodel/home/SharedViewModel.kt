package com.snapco.techlife.ui.viewmodel.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snapco.techlife.data.model.home.Post

class SharedViewModel : ViewModel() {
    private val _newPost = MutableLiveData<Post?>()
    val newPost: LiveData<Post?> = _newPost

    private val _postList = MutableLiveData<MutableList<Post>>(mutableListOf())
    val postList: LiveData<MutableList<Post>> = _postList

    // Method to add a new post
    fun setNewPost(post: Post) {
        val updatedList = _postList.value?.toMutableList() ?: mutableListOf()
        updatedList.add(0, post)  // Add the new post at the beginning
        _postList.value = updatedList
        _newPost.value = post
    }

    // Method to update a post at a specific position
    fun updatePost(updatedPost: Post, position: Int) {
        val currentList = _postList.value
        if (currentList != null && position >= 0 && position < currentList.size) {
            val mutableList = currentList.toMutableList()  // Convert to MutableList to modify
            mutableList[position] = updatedPost  // Update the post at the specified position
            _postList.value = mutableList  // Update LiveData with the modified list
        } else {
            Log.e("SharedViewModel", "Invalid position or list is empty, cannot update post.")
        }
    }

    // Method to delete a post at a specific position
    fun deletePost(position: Int) {
        val currentList = _postList.value
        if (!currentList.isNullOrEmpty() && position >= 0 && position < currentList.size) {
            val mutableList = currentList.toMutableList()  // Convert to MutableList
            mutableList.removeAt(position)  // Remove the post at the given position
            _postList.value = mutableList  // Update LiveData with the modified list
        } else {
            Log.e("SharedViewModel", "Invalid position or list is empty, cannot delete post.")
        }
    }

    // Clear new post data
    fun clearNewPost() {
        _newPost.value = null
    }
}
