package com.snapco.techlife.ui.viewmodel.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snapco.techlife.data.model.home.Feed


class SharedViewModel : ViewModel() {
    private val _newPost = MutableLiveData<Feed?>()
    val newPost: LiveData<Feed?> = _newPost

    private val _feedList = MutableLiveData<MutableList<Feed>>(mutableListOf())
    val feedList: LiveData<MutableList<Feed>> = _feedList

    // Method to add a new post
    fun setNewPost(post: Feed) {
        val updatedList = _feedList.value?.toMutableList() ?: mutableListOf()
        updatedList.add(0, post)  // Add the new post at the beginning
        _feedList.value = updatedList
        _newPost.value = post
    }

    fun updatePost(updatedPost: Feed, position: Int) {
        // Ensure the position is valid and the list is not empty
        val currentList = _feedList.value
        if (currentList != null && position >= 0 && position < currentList.size) {
            val mutableList = currentList.toMutableList()  // Convert to MutableList to modify
            mutableList[position] = updatedPost  // Update the post at the specified position
            _feedList.value = mutableList  // Update LiveData with the modified list
        } else {
            Log.e("SharedViewModel", "Invalid position or list is empty, cannot update post.")
        }
    }

    fun deletePost(position: Int) {
        // Ensure that the position is valid and the list is not empty
        val currentList = _feedList.value
        if (!currentList.isNullOrEmpty() && position >= 0 && position < currentList.size) {
            val mutableList = currentList.toMutableList()  // Convert to MutableList
            mutableList.removeAt(position)  // Remove the post at the given position
            _feedList.value = mutableList  // Update the LiveData with the modified list
        } else {
            Log.e("SharedViewModel", "Invalid position or list is empty, cannot delete post.")
        }
    }

    // Clear new post data
    fun clearNewPost() {
        _newPost.value = null
    }
}
